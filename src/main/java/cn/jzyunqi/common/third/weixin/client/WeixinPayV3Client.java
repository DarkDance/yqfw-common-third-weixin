package cn.jzyunqi.common.third.weixin.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.pay.OrderQueryResult;
import cn.jzyunqi.common.feature.pay.OrderRefundResult;
import cn.jzyunqi.common.feature.redis.Cache;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.weixin.client.interceptor.WeixinPayV3HeaderInterceptor;
import cn.jzyunqi.common.third.weixin.model.callback.PayResultCb;
import cn.jzyunqi.common.third.weixin.model.enums.RefundStatus;
import cn.jzyunqi.common.third.weixin.model.enums.TradeState;
import cn.jzyunqi.common.third.weixin.model.redis.PlantCertRedisDto;
import cn.jzyunqi.common.third.weixin.model.request.RefundOrderParam;
import cn.jzyunqi.common.third.weixin.model.request.UnifiedOrderParam;
import cn.jzyunqi.common.third.weixin.model.response.OrderQueryV3Rsp;
import cn.jzyunqi.common.third.weixin.model.response.OrderRefundV3Rsp;
import cn.jzyunqi.common.third.weixin.model.response.PlantCertRsp;
import cn.jzyunqi.common.third.weixin.model.response.UnifiedOrderV3Rsp;
import cn.jzyunqi.common.third.weixin.model.response.item.PlantCertData;
import cn.jzyunqi.common.third.weixin.model.response.item.PrepayIdData;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author wiiyaya
 * @date 2018/5/29.
 */
@Slf4j
public class WeixinPayV3Client {

    private static final String COMMON_ERROR_WX_UNIFIED_ORDER_ERROR = "common_error_wx_unified_order_error";
    private static final String COMMON_ERROR_WX_PLANT_CERT_ERROR = "common_error_wx_plant_cert_error";

    /**
     * 微信支付V3域名
     */
    private static final String WX_PAY_DOMAIN = "https://api.mch.weixin.qq.com";

    /**
     * 接口：平台证书列表
     */
    private static final String WX_PAY_PLANT_CERT_URL = "/v3/certificates";

    /**
     * 接口：小程序下单
     */
    private static final String WX_PAY_JSAPI_ORDER_URL = "/v3/pay/transactions/jsapi";

    /**
     * 接口：支付订单查询
     */
    private static final String WX_PAY_ORDER_QUERY_WX_URL = "/v3/pay/transactions/id/%s?mchid=%s";
    private static final String WX_PAY_ORDER_QUERY_LOCAL_URL = "/v3/pay/transactions/out-trade-no/%s?mchid=%s";

    /**
     * 接口：申请退款
     */
    private static final String WX_PAY_ORDER_REFUND_URL = "/v3/refund/domestic/refunds";

    /**
     * 应用唯一标识
     */
    private final String appId;

    /**
     * 支付回调URL
     */
    private final String payCallbackUrl;

    /**
     * 退款回调URL
     */
    private final String refundCallbackUrl;

    /**
     * 支付商户号
     */
    private final String merchantId;

    /**
     * 支付商户证书私钥
     */
    private final String merchantPrivateKey;

    /**
     * 支付商户证书序列号
     */
    private final String merchantSerialNumber;

    /**
     * 支付商户AES key
     */
    private final String merchantAesKey;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    private final RedisHelper redisHelper;

    private final Cache pemCache;

    public WeixinPayV3Client(String appId, String merchantId, String merchantPrivateKey, String merchantSerialNumber, String merchantAesKey, String payCallbackUrl, String refundCallbackUrl, RedisHelper redisHelper, Cache pemCache) throws Exception {
        this.appId = appId;
        this.merchantId = merchantId;
        this.merchantPrivateKey = merchantPrivateKey;
        this.merchantSerialNumber = merchantSerialNumber;
        this.merchantAesKey = merchantAesKey;
        this.payCallbackUrl = payCallbackUrl;
        this.refundCallbackUrl = refundCallbackUrl;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.redisHelper = redisHelper;
        this.pemCache = pemCache;

        this.objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, Boolean.TRUE);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, Boolean.FALSE);
        this.objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(this.objectMapper);
        jsonConverter.setSupportedMediaTypes(CollectionUtilPlus.Array.asList(MediaType.APPLICATION_JSON));

        this.restTemplate.setMessageConverters(CollectionUtilPlus.Array.asList(jsonConverter));
        this.restTemplate.getInterceptors().add(new WeixinPayV3HeaderInterceptor(this, CollectionUtilPlus.Array.asList(WX_PAY_PLANT_CERT_URL)));
    }

    @PostConstruct
    public void plantCertInit() throws BusinessException {
        //构造完成立即调用一次
        this.plantCert(null);
    }

    /**
     * 微信下单
     *
     * @param outTradeNo       本地单号
     * @param simpleDesc       简单描述
     * @param amount           订单总金额
     * @param expiresInMinutes 几分钟过期
     * @param openId           支付人微信id
     * @return 支付签名
     */
    public UnifiedOrderV3Rsp signForPay(String outTradeNo, String simpleDesc, BigDecimal amount, int expiresInMinutes, String openId) throws BusinessException {
        UnifiedOrderParam unifiedOrderParam = new UnifiedOrderParam();
        unifiedOrderParam.setAppId(appId);
        unifiedOrderParam.setMchId(merchantId);
        unifiedOrderParam.setDescription(StringUtilPlus.substring(StringUtilPlus.replaceEmoji(simpleDesc).toString(), 0, 128));
        unifiedOrderParam.setOutTradeNo(outTradeNo);
        unifiedOrderParam.setTimeExpire(OffsetDateTime.now(DateTimeUtilPlus.CHINA_ZONE_ID).plusMinutes(expiresInMinutes));
        unifiedOrderParam.setNotifyUrl(payCallbackUrl);
        unifiedOrderParam.getAmount().setTotal(amount.multiply(new BigDecimal(100)).intValue());
        unifiedOrderParam.getPayer().setOpenId(openId);

        try {
            HttpHeaders headers = new HttpHeaders();
            this.headerSign(headers, HttpMethod.POST, WX_PAY_JSAPI_ORDER_URL, objectMapper.writeValueAsString(unifiedOrderParam));

            URI uri = new URIBuilder(WX_PAY_DOMAIN + WX_PAY_JSAPI_ORDER_URL).build();
            RequestEntity<UnifiedOrderParam> requestEntity = new RequestEntity<>(unifiedOrderParam, headers, HttpMethod.POST, uri);
            ResponseEntity<PrepayIdData> sendRsp = restTemplate.exchange(requestEntity, PrepayIdData.class);
            PrepayIdData prepayIdData = Optional.ofNullable(sendRsp.getBody()).orElseGet(PrepayIdData::new);
            String pkg = "prepay_id=" + prepayIdData.getPrepayId();

            String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
            Long timestamp = System.currentTimeMillis() / 1000;
            String needSignContent = StringUtilPlus.join(
                    appId, StringUtilPlus.ENTER,
                    timestamp, StringUtilPlus.ENTER,
                    nonceStr, StringUtilPlus.ENTER,
                    pkg, StringUtilPlus.ENTER
            );
            String sign = DigestUtilPlus.RSA256.signPrivateKey(needSignContent.getBytes(StringUtilPlus.UTF_8), DigestUtilPlus.Base64.decodeBase64(merchantPrivateKey), Boolean.TRUE);

            UnifiedOrderV3Rsp unifiedOrderV3Rsp = new UnifiedOrderV3Rsp();
            unifiedOrderV3Rsp.setNonceStr(nonceStr);
            unifiedOrderV3Rsp.setTimeStamp(timestamp.toString());
            unifiedOrderV3Rsp.setWeixinPackage(pkg);
            unifiedOrderV3Rsp.setSignType("RSA");
            unifiedOrderV3Rsp.setPaySign(sign);
            unifiedOrderV3Rsp.setApplyPayNo(outTradeNo);
            return unifiedOrderV3Rsp;

        } catch (HttpClientErrorException e) {
            try {
                PlantCertRsp errorRsp = objectMapper.readValue(e.getResponseBodyAsString(), PlantCertRsp.class);
                throw new BusinessException(e, COMMON_ERROR_WX_UNIFIED_ORDER_ERROR, errorRsp.getCode(), errorRsp.getMessage());
            } catch (IOException e1) {
                throw new BusinessException(e, COMMON_ERROR_WX_UNIFIED_ORDER_ERROR, "-1", "readValue error");
            }
        } catch (Exception e) {
            throw new BusinessException(e, COMMON_ERROR_WX_UNIFIED_ORDER_ERROR, "-2", "unknown error");
        }
    }

    /**
     * 支付回调解码
     *
     * @param returnHeaderMap 回调头
     * @param returnParam     回调体
     * @param payResultCb     回调密文
     * @return 解码结果
     */
    public OrderQueryResult decryptPayCallback(Map<String, String> returnHeaderMap, String returnParam, PayResultCb payResultCb) {
        try {
            this.verifyHeader(returnHeaderMap, returnParam);

            String cipherText = payResultCb.getResource().getCipherText();
            String nonce = payResultCb.getResource().getNonce();
            String associatedData = payResultCb.getResource().getAssociatedData();
            String realCallback = DigestUtilPlus.AES.decryptGCM(
                    DigestUtilPlus.Base64.decodeBase64(cipherText),
                    merchantAesKey.getBytes(StringUtilPlus.UTF_8),
                    nonce.getBytes(StringUtilPlus.UTF_8),
                    associatedData.getBytes(StringUtilPlus.UTF_8)
            );
            OrderQueryV3Rsp orderQueryV3Rsp = objectMapper.readValue(realCallback, OrderQueryV3Rsp.class);
            if (orderQueryV3Rsp.getTradeState() == TradeState.SUCCESS) {
                OrderQueryResult dto = new OrderQueryResult();
                dto.setOutTradeNo(orderQueryV3Rsp.getOutTradeNo()); //商户订单号
                dto.setTransactionId(orderQueryV3Rsp.getTransactionId()); //微信支付订单号
                dto.setTotalFee(new BigDecimal(orderQueryV3Rsp.getAmount().getPayerTotal()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN)); //订单总金额，单位为分
                dto.setResponseStr("接口回调:" + objectMapper.writeValueAsString(orderQueryV3Rsp));
                return dto;
            } else {
                log.error("======WeixinPayV3Helper decryptPayCallback result[{}] not success:", objectMapper.writeValueAsString(orderQueryV3Rsp));
                return null;
            }
        } catch (Exception e) {
            log.error("======WeixinPayV3Helper decryptPayCallback error:", e);
            return null;
        }
    }

    /**
     * 根据单号查询支付结果
     *
     * @param transactionId 微信单号
     * @param outTradeNo    本地单号
     * @return 订单结果
     */
    public OrderQueryResult queryPay(String transactionId, String outTradeNo) {
        try {
            String requestUrl;
            if (StringUtilPlus.isNotEmpty(transactionId)) {
                requestUrl = String.format(WX_PAY_ORDER_QUERY_WX_URL, transactionId, merchantId);
            } else {
                requestUrl = String.format(WX_PAY_ORDER_QUERY_LOCAL_URL, outTradeNo, merchantId);
            }

            HttpHeaders headers = new HttpHeaders();
            this.headerSign(headers, HttpMethod.GET, requestUrl, StringUtilPlus.EMPTY);

            URI uri = new URIBuilder(WX_PAY_DOMAIN + requestUrl).build();
            RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);
            ResponseEntity<OrderQueryV3Rsp> sendRsp = restTemplate.exchange(requestEntity, OrderQueryV3Rsp.class);
            OrderQueryV3Rsp orderQueryRsp = Optional.ofNullable(sendRsp.getBody()).orElseGet(OrderQueryV3Rsp::new);

            if (orderQueryRsp.getTradeState() == TradeState.SUCCESS) {
                OrderQueryResult dto = new OrderQueryResult();
                dto.setOutTradeNo(orderQueryRsp.getOutTradeNo()); //商户订单号
                dto.setTransactionId(orderQueryRsp.getTransactionId()); //微信支付订单号
                dto.setTotalFee(new BigDecimal(orderQueryRsp.getAmount().getPayerTotal()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN)); //订单总金额，单位为分
                dto.setResponseStr("主动请求:" + objectMapper.writeValueAsString(orderQueryRsp));
                return dto;
            } else {
                log.error("======WeixinPayV3Helper queryPay result[{}] not success:", objectMapper.writeValueAsString(orderQueryRsp));
                return null;
            }
        } catch (Exception e) {
            log.error("======WeixinPayV3Helper queryPay error:", e);
            return null;
        }
    }

    /**
     * 退款回调解码
     *
     * @param payResultCb 回调密文
     * @return 解码结果
     */
    public OrderQueryResult decryptRefundCallback(PayResultCb payResultCb) {
        try {
            String cipherText = payResultCb.getResource().getCipherText();
            String nonce = payResultCb.getResource().getNonce();
            String associatedData = payResultCb.getResource().getAssociatedData();
            String realCallback = DigestUtilPlus.AES.decryptGCM(
                    DigestUtilPlus.Base64.decodeBase64(cipherText),
                    merchantAesKey.getBytes(StringUtilPlus.UTF_8),
                    nonce.getBytes(StringUtilPlus.UTF_8),
                    associatedData.getBytes(StringUtilPlus.UTF_8)
            );
            OrderRefundV3Rsp orderRefundV3Rsp = objectMapper.readValue(realCallback, OrderRefundV3Rsp.class);
            if (orderRefundV3Rsp.getStatus() == RefundStatus.SUCCESS) {
                OrderQueryResult dto = new OrderQueryResult();
                dto.setOutTradeNo(orderRefundV3Rsp.getOutTradeNo()); //商户订单号
                dto.setTransactionId(orderRefundV3Rsp.getTransactionId()); //微信支付订单号
                dto.setTotalFee(new BigDecimal(orderRefundV3Rsp.getAmount().getPayerTotal()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN)); //订单总金额，单位为分
                dto.setResponseStr(objectMapper.writeValueAsString(orderRefundV3Rsp));
                return dto;
            } else {
                log.error("======WeixinPayV3Helper decryptRefundCallback result[{}] not success:", objectMapper.writeValueAsString(orderRefundV3Rsp));
                return null;
            }
        } catch (Exception e) {
            log.error("======WeixinPayV3Helper decryptRefundCallback error:", e);
            return null;
        }
    }

    /**
     * 微信退款
     *
     * @param transactionId 微信单号
     * @param outRefundNo   本地退单号
     * @param totalFee      微信单号金额
     * @param refundFee     需要退款金额
     * @param refundDesc    退款描述
     * @return 退款结果
     */
    public OrderRefundResult payRefund(String transactionId, String outRefundNo, BigDecimal totalFee, BigDecimal refundFee, String refundDesc) throws BusinessException {
        RefundOrderParam refundOrderParam = new RefundOrderParam();
        refundOrderParam.setTransactionId(transactionId);
        refundOrderParam.setOutRefundNo(outRefundNo);
        refundOrderParam.setReason(refundDesc);
        refundOrderParam.setNotifyUrl(refundCallbackUrl);
        refundOrderParam.getAmount().setRefund(refundFee.multiply(new BigDecimal(100)).intValue());
        refundOrderParam.getAmount().setTotal(totalFee.multiply(new BigDecimal(100)).intValue());
        refundOrderParam.getAmount().setCurrency("CNY");

        try {
            HttpHeaders headers = new HttpHeaders();
            this.headerSign(headers, HttpMethod.POST, WX_PAY_ORDER_REFUND_URL, objectMapper.writeValueAsString(refundOrderParam));


            URI uri = new URIBuilder(WX_PAY_DOMAIN + WX_PAY_ORDER_REFUND_URL).build();
            RequestEntity<RefundOrderParam> requestEntity = new RequestEntity<>(refundOrderParam, headers, HttpMethod.POST, uri);
            ResponseEntity<OrderRefundV3Rsp> sendRsp = restTemplate.exchange(requestEntity, OrderRefundV3Rsp.class);
            OrderRefundV3Rsp orderRefundRsp = Optional.ofNullable(sendRsp.getBody()).orElseGet(OrderRefundV3Rsp::new);

            if (orderRefundRsp.getStatus() == RefundStatus.SUCCESS) {
                OrderRefundResult dto = new OrderRefundResult();
                dto.setRefundId(orderRefundRsp.getRefundId());
                dto.setRefundFee(new BigDecimal(orderRefundRsp.getAmount().getPayerRefund()).divide(new BigDecimal(100), 2, RoundingMode.DOWN));
                dto.setResponseStr(objectMapper.writeValueAsString(orderRefundRsp));
                return dto;
            } else {
                log.error("======WeixinPayV3Helper payRefund result[{}] not success:", objectMapper.writeValueAsString(orderRefundRsp));
                throw new BusinessException("common_error_wx_refund_failed");
            }
        } catch (Exception e) {
            throw new BusinessException(e, "common_error_wx_refund_error");
        }
    }

    /**
     * 获取微信支付平台证书
     *
     * @param weixinPemSerial 微信证书序列号
     * @return 平台公钥
     */
    public PlantCertRedisDto plantCert(String weixinPemSerial) throws BusinessException {
        Map<String, Object> weixinPem = redisHelper.hGetAll(pemCache, merchantId);
        if (CollectionUtilPlus.Map.isNotEmpty(weixinPem) && StringUtilPlus.isBlank(weixinPemSerial)) {
            return null;
        }
        PlantCertRedisDto plantCertRedisDto = switchNewestPem(weixinPem, weixinPemSerial);
        if (plantCertRedisDto != null && LocalDateTime.now().isBefore(plantCertRedisDto.getExpireTime())) {
            return plantCertRedisDto;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            this.headerSign(headers, HttpMethod.GET, WX_PAY_PLANT_CERT_URL, StringUtilPlus.EMPTY);

            PlantCertRsp plantCertRsp;

            URI uri = new URIBuilder(WX_PAY_DOMAIN + WX_PAY_PLANT_CERT_URL).build();
            RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);
            ResponseEntity<PlantCertRsp> response = restTemplate.exchange(requestEntity, PlantCertRsp.class);

            plantCertRsp = Optional.ofNullable(response.getBody()).orElseGet(PlantCertRsp::new);
            weixinPem = new HashMap<>();
            for (PlantCertData certData : plantCertRsp.getData()) {
                String cipherText = certData.getEncryptCertificate().getCipherText();
                String nonce = certData.getEncryptCertificate().getNonce();
                String associatedData = certData.getEncryptCertificate().getAssociatedData();
                String pem = DigestUtilPlus.AES.decryptGCM(
                        DigestUtilPlus.Base64.decodeBase64(cipherText),
                        merchantAesKey.getBytes(StringUtilPlus.UTF_8),
                        nonce.getBytes(StringUtilPlus.UTF_8),
                        associatedData.getBytes(StringUtilPlus.UTF_8)
                );

                //通过服务器证书获取服务器支付公钥
                CertificateFactory cf = CertificateFactory.getInstance("X509");
                X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(pem.getBytes(StringUtilPlus.UTF_8)));
                x509Cert.checkValidity();
                String weixinPublicKey = DigestUtilPlus.Base64.encodeBase64String(x509Cert.getPublicKey().getEncoded());

                //存入缓存
                plantCertRedisDto = new PlantCertRedisDto();
                plantCertRedisDto.setSerialNo(certData.getSerialNo());
                plantCertRedisDto.setEffectiveTime(certData.getEffectiveTime().toLocalDateTime());
                plantCertRedisDto.setExpireTime(certData.getExpireTime().toLocalDateTime());
                plantCertRedisDto.setPublicKey(weixinPublicKey);

                weixinPem.put(certData.getSerialNo(), plantCertRedisDto);
            }
            redisHelper.hPutAll(pemCache, merchantId, weixinPem);

            return switchNewestPem(weixinPem, weixinPemSerial);
        } catch (HttpClientErrorException e) {
            try {
                PlantCertRsp errorRsp = objectMapper.readValue(e.getResponseBodyAsString(), PlantCertRsp.class);
                throw new BusinessException(e, COMMON_ERROR_WX_PLANT_CERT_ERROR, errorRsp.getCode(), errorRsp.getMessage());
            } catch (IOException e1) {
                throw new BusinessException(e, COMMON_ERROR_WX_PLANT_CERT_ERROR, "-1", "readValue error");
            }
        } catch (Exception e) {
            throw new BusinessException(e, COMMON_ERROR_WX_PLANT_CERT_ERROR, "-2", "unknown error");
        }
    }

    /**
     * 选择最新的证书
     *
     * @param weixinPem       证书map
     * @param weixinPemSerial 选用的证书序号
     * @return 最新的证书
     */
    private PlantCertRedisDto switchNewestPem(Map<String, Object> weixinPem, String weixinPemSerial) {
        return weixinPem != null ? weixinPem.values().stream()
                .map(o -> (PlantCertRedisDto) o)
                .filter(plantCertRedisDto -> StringUtilPlus.equals(plantCertRedisDto.getSerialNo(), weixinPemSerial))
                .findFirst()
                .orElse(null) : null;
    }


    /**
     * 向请求头添加签名
     *
     * @param headers     头信息
     * @param method      请求方法
     * @param requestPath 请求路径（不含域名）
     * @param requestBody 请求体
     */
    private void headerSign(HttpHeaders headers, HttpMethod method, String requestPath, String requestBody) throws Exception {
        Long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
        String needSignContent = StringUtilPlus.join(
                method, StringUtilPlus.ENTER,
                requestPath, StringUtilPlus.ENTER,
                timestamp, StringUtilPlus.ENTER,
                nonceStr, StringUtilPlus.ENTER,
                StringUtilPlus.defaultIfBlank(requestBody, StringUtilPlus.EMPTY), StringUtilPlus.ENTER
        );
        String sign = DigestUtilPlus.RSA256.signPrivateKey(needSignContent.getBytes(StringUtilPlus.UTF_8), DigestUtilPlus.Base64.decodeBase64(merchantPrivateKey), Boolean.TRUE);
        headers.set("Authorization", String.format(
                "WECHATPAY2-SHA256-RSA2048 mchid=\"%s\",serial_no=\"%s\",nonce_str=\"%s\",timestamp=\"%s\",signature=\"%s\"",
                merchantId,
                merchantSerialNumber,
                nonceStr,
                timestamp,
                sign
        ));
    }

    /**
     * 校验头信息
     *
     * @param returnHeaderMap 头信息
     * @param returnParam     体信息
     */
    public void verifyHeader(Map<String, String> returnHeaderMap, String returnParam) throws SSLException {
        String weixinSign = returnHeaderMap.get("Wechatpay-Signature");
        String weixinPemSerial = returnHeaderMap.get("Wechatpay-Serial");
        String timestamp = returnHeaderMap.get("Wechatpay-Timestamp");
        String nonce = returnHeaderMap.get("Wechatpay-Nonce");

        long currTimeStamp = System.currentTimeMillis() / 1000;
        if(currTimeStamp - 600 > Long.parseLong(timestamp) || currTimeStamp + 600 < Long.parseLong(timestamp)){
            throw new SSLException("sign currTimeStamp verify failed!");
        }

        String waitSign = String.format("%s\n%s\n%s\n", timestamp, nonce, returnParam);
        //获取证书
        try {
            PlantCertRedisDto plantCertRedisDto = this.plantCert(weixinPemSerial);
            boolean matchResult = DigestUtilPlus.RSA256.verifySignPublicKey(waitSign.getBytes(StringUtilPlus.UTF_8), weixinSign, DigestUtilPlus.Base64.decodeBase64(plantCertRedisDto.getPublicKey()));
            if (!matchResult) {
                throw new SSLException("sign not match!");
            }
        } catch (Exception e) {
            throw new SSLException("sign verify failed!", e);
        }
    }
}
