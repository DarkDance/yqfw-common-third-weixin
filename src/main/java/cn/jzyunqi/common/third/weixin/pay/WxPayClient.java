package cn.jzyunqi.common.third.weixin.pay;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.weixin.common.constant.WxCache;
import cn.jzyunqi.common.third.weixin.common.enums.WeixinPaySubType;
import cn.jzyunqi.common.third.weixin.common.utils.WxFormatUtils;
import cn.jzyunqi.common.third.weixin.pay.callback.model.WxPayResultCb;
import cn.jzyunqi.common.third.weixin.pay.cert.WxPayCertApiProxy;
import cn.jzyunqi.common.third.weixin.pay.cert.model.PlantCertData;
import cn.jzyunqi.common.third.weixin.pay.cert.model.PlantCertRedisDto;
import cn.jzyunqi.common.third.weixin.pay.order.WxPayOrderApiProxy;
import cn.jzyunqi.common.third.weixin.pay.order.enums.RefundStatus;
import cn.jzyunqi.common.third.weixin.pay.order.enums.TradeState;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderRefundData;
import cn.jzyunqi.common.third.weixin.pay.order.model.PayAmountData;
import cn.jzyunqi.common.third.weixin.pay.order.model.PayPayerData;
import cn.jzyunqi.common.third.weixin.pay.order.model.RefundOrderParam;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedAppOrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedH5OrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedJsapiOrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedOrderParam;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedOrderRsp;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@Slf4j
public class WxPayClient {

    @Resource
    private WxPayOrderApiProxy wxPayOrderApiProxy;

    @Resource
    private WxPayCertApiProxy wxPayCertApiProxy;

    @Resource
    private RedisHelper redisHelper;

    public final Order order = new Order();
    public final Callback cb = new Callback();

    @Resource
    private WxPayAuthRepository wxPayAuthRepository;

    @PostConstruct
    public void plantCertInit() throws BusinessException {
        for (WxPayAuth wxPayAuth : wxPayAuthRepository.getWxPayAuthList()) {
            //构造完成立即调用一次
            this.plantCert(wxPayAuth, null);
        }
    }

    public class Order {

        //JSAPI、小程序支付 - 预下单
        public UnifiedJsapiOrderData unifiedJsapiOrder(String wxAppId, WeixinPaySubType paySubType, String outTradeNo, String simpleDesc, BigDecimal amount, int expiresInMinutes, String openId) throws BusinessException {
            WxPayAuth wxPayAuth = wxPayAuthRepository.choosWxPayAuth(wxAppId);
            String appId = switch (paySubType.getWeixinType()) {
                case OPEN, MINI_APP -> null;
                case MP, PC -> wxPayAuth.getWxAppId();
            };

            UnifiedOrderParam unifiedOrderParam = new UnifiedOrderParam();
            unifiedOrderParam.setAppId(appId);
            unifiedOrderParam.setMchId(wxPayAuth.getMerchantId());
            unifiedOrderParam.setDescription(StringUtilPlus.substring(StringUtilPlus.replaceEmoji(simpleDesc).toString(), 0, 128));
            unifiedOrderParam.setOutTradeNo(outTradeNo);
            unifiedOrderParam.setTimeExpire(ZonedDateTime.now(DateTimeUtilPlus.CHINA_ZONE_ID).plusMinutes(expiresInMinutes));
            unifiedOrderParam.setNotifyUrl(wxPayAuth.getPayCallbackUrl());

            PayAmountData payAmountData = new PayAmountData();
            payAmountData.setTotal(amount.multiply(new BigDecimal(100)).intValue());
            unifiedOrderParam.setAmount(payAmountData);

            PayPayerData payer = new PayPayerData();
            payer.setOpenId(openId);
            unifiedOrderParam.setPayer(payer);

            UnifiedOrderRsp unifiedOrderTempRsp = wxPayOrderApiProxy.unifiedJsapiOrder(wxPayAuth.getWxAppId(), unifiedOrderParam);
            String pkg = "prepay_id=" + unifiedOrderTempRsp.getPrepayId();

            String nonceStr = RandomUtilPlus.String.nextAlphanumeric(32);
            Long timestamp = System.currentTimeMillis() / 1000;
            String needSignContent = StringUtilPlus.join(
                    appId, StringUtilPlus.ENTER,
                    timestamp, StringUtilPlus.ENTER,
                    nonceStr, StringUtilPlus.ENTER,
                    pkg, StringUtilPlus.ENTER
            );
            String sign = null;
            try {
                sign = DigestUtilPlus.RSA256.signPrivateKey(needSignContent.getBytes(StringUtilPlus.UTF_8), DigestUtilPlus.Base64.decodeBase64(wxPayAuth.getMerchantPrivateKey()), Boolean.TRUE);
            } catch (Exception e) {
                log.error("=====unifiedJsapiOrder sign error", e);
            }

            UnifiedJsapiOrderData unifiedOrderV3Rsp = new UnifiedJsapiOrderData();
            unifiedOrderV3Rsp.setAppId(appId);
            unifiedOrderV3Rsp.setNonceStr(nonceStr);
            unifiedOrderV3Rsp.setTimeStamp(timestamp.toString());
            unifiedOrderV3Rsp.setPackageValue(pkg);
            unifiedOrderV3Rsp.setSignType("RSA");
            unifiedOrderV3Rsp.setPaySign(sign);
            unifiedOrderV3Rsp.setApplyPayNo(outTradeNo);
            return unifiedOrderV3Rsp;
        }

        //小程序支付 - 订单查询
        public OrderData queryOrder(String wxAppId, String transactionId, String outTradeNo) throws BusinessException {
            WxPayAuth wxPayAuth = wxPayAuthRepository.choosWxPayAuth(wxAppId);
            OrderData orderData;
            if (StringUtilPlus.isNotEmpty(transactionId)) {
                orderData = wxPayOrderApiProxy.queryOrderByTransactionId(wxPayAuth.getWxAppId(), transactionId, wxPayAuth.getMerchantId());
            } else {
                orderData = wxPayOrderApiProxy.queryOrderByOutTradeNo(wxPayAuth.getWxAppId(), outTradeNo, wxPayAuth.getMerchantId());
            }
            return orderData;
        }

        //小程序支付 - 退款申请
        public OrderRefundData refundApply(String wxAppId, String transactionId, String outRefundNo, BigDecimal totalFee, BigDecimal refundFee, String refundDesc) throws BusinessException {
            WxPayAuth wxPayAuth = wxPayAuthRepository.choosWxPayAuth(wxAppId);
            RefundOrderParam refundOrderParam = new RefundOrderParam();
            refundOrderParam.setTransactionId(transactionId);
            refundOrderParam.setOutRefundNo(outRefundNo);
            refundOrderParam.setReason(refundDesc);
            refundOrderParam.setNotifyUrl(wxPayAuth.getRefundCallbackUrl());
            refundOrderParam.getAmount().setRefund(refundFee.multiply(new BigDecimal(100)).intValue());
            refundOrderParam.getAmount().setTotal(totalFee.multiply(new BigDecimal(100)).intValue());
            refundOrderParam.getAmount().setCurrency("CNY");

            OrderRefundData orderRefundData = wxPayOrderApiProxy.refundApply(wxPayAuth.getWxAppId(), refundOrderParam);
            orderRefundData.setActualRefundAmount(BigDecimal.valueOf(orderRefundData.getAmount().getPayerRefund()).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN));
            try {
                orderRefundData.setResponseStr(WxFormatUtils.OBJECT_MAPPER.writeValueAsString(orderRefundData));
            } catch (JsonProcessingException e) {
                log.warn("=====refundApply orderRefundData parse error", e);
            }
            return orderRefundData;
        }

        //Native支付 - 预下单
        public UnifiedH5OrderData unifiedNativeOrder(String wxAppId, String outTradeNo, String simpleDesc, BigDecimal amount, int expiresInMinutes) throws BusinessException {
            WxPayAuth wxPayAuth = wxPayAuthRepository.choosWxPayAuth(wxAppId);
            UnifiedOrderParam unifiedOrderParam = new UnifiedOrderParam();
            unifiedOrderParam.setAppId(wxPayAuth.getWxAppId());//只能是公众号的appId
            unifiedOrderParam.setMchId(wxPayAuth.getMerchantId());
            unifiedOrderParam.setDescription(StringUtilPlus.substring(StringUtilPlus.replaceEmoji(simpleDesc).toString(), 0, 128));
            unifiedOrderParam.setOutTradeNo(outTradeNo);
            unifiedOrderParam.setTimeExpire(ZonedDateTime.now(DateTimeUtilPlus.CHINA_ZONE_ID).plusMinutes(expiresInMinutes));
            unifiedOrderParam.setNotifyUrl(wxPayAuth.getPayCallbackUrl());

            PayAmountData payAmountData = new PayAmountData();
            payAmountData.setTotal(amount.multiply(new BigDecimal(100)).intValue());
            unifiedOrderParam.setAmount(payAmountData);

            String codeUrl = wxPayOrderApiProxy.unifiedNativeOrder(wxPayAuth.getWxAppId(), unifiedOrderParam).getCodeUrl();
            UnifiedH5OrderData h5OrderData = new UnifiedH5OrderData();
            h5OrderData.setApplyPayNo(outTradeNo);
            h5OrderData.setUrl(codeUrl);
            return h5OrderData;
        }

        //H5支付 - 预下单
        public UnifiedH5OrderData unifiedH5Order(String wxAppId, String outTradeNo, String simpleDesc, BigDecimal amount, int expiresInMinutes) throws BusinessException {
            WxPayAuth wxPayAuth = wxPayAuthRepository.choosWxPayAuth(wxAppId);
            UnifiedOrderParam unifiedOrderParam = new UnifiedOrderParam();
            unifiedOrderParam.setAppId(wxPayAuth.getWxAppId());//只能是公众号的appId
            unifiedOrderParam.setMchId(wxPayAuth.getMerchantId());
            unifiedOrderParam.setDescription(StringUtilPlus.substring(StringUtilPlus.replaceEmoji(simpleDesc).toString(), 0, 128));
            unifiedOrderParam.setOutTradeNo(outTradeNo);
            unifiedOrderParam.setTimeExpire(ZonedDateTime.now(DateTimeUtilPlus.CHINA_ZONE_ID).plusMinutes(expiresInMinutes));
            unifiedOrderParam.setNotifyUrl(wxPayAuth.getPayCallbackUrl());

            PayAmountData payAmountData = new PayAmountData();
            payAmountData.setTotal(amount.multiply(new BigDecimal(100)).intValue());
            unifiedOrderParam.setAmount(payAmountData);

            String h5Url = wxPayOrderApiProxy.unifiedH5Order(wxPayAuth.getWxAppId(), unifiedOrderParam).getH5Url();

            UnifiedH5OrderData h5OrderData = new UnifiedH5OrderData();
            h5OrderData.setApplyPayNo(outTradeNo);
            h5OrderData.setUrl(h5Url);
            return h5OrderData;
        }

        //APP支付 - 预下单
        public UnifiedAppOrderData unifiedAppOrder(String wxAppId, String outTradeNo, String simpleDesc, BigDecimal amount, int expiresInMinutes) throws BusinessException {
            WxPayAuth wxPayAuth = wxPayAuthRepository.choosWxPayAuth(wxAppId);
            UnifiedOrderParam unifiedOrderParam = new UnifiedOrderParam();
            unifiedOrderParam.setAppId(wxPayAuth.getWxAppId());//只能是开放平台的appId
            unifiedOrderParam.setMchId(wxPayAuth.getMerchantId());
            unifiedOrderParam.setDescription(StringUtilPlus.substring(StringUtilPlus.replaceEmoji(simpleDesc).toString(), 0, 128));
            unifiedOrderParam.setOutTradeNo(outTradeNo);
            unifiedOrderParam.setTimeExpire(ZonedDateTime.now(DateTimeUtilPlus.CHINA_ZONE_ID).plusMinutes(expiresInMinutes));
            unifiedOrderParam.setNotifyUrl(wxPayAuth.getPayCallbackUrl());

            PayAmountData payAmountData = new PayAmountData();
            payAmountData.setTotal(amount.multiply(new BigDecimal(100)).intValue());
            unifiedOrderParam.setAmount(payAmountData);

            String prepayId = wxPayOrderApiProxy.unifiedAppOrder(wxPayAuth.getWxAppId(), unifiedOrderParam).getPrepayId();

            String nonceStr = RandomUtilPlus.String.nextAlphanumeric(32);
            Long timestamp = System.currentTimeMillis() / 1000;
            String needSignContent = StringUtilPlus.join(
                    wxPayAuth.getWxAppId(), StringUtilPlus.ENTER,
                    timestamp, StringUtilPlus.ENTER,
                    nonceStr, StringUtilPlus.ENTER,
                    prepayId, StringUtilPlus.ENTER
            );
            String sign = null;
            try {
                sign = DigestUtilPlus.RSA256.signPrivateKey(needSignContent.getBytes(StringUtilPlus.UTF_8), DigestUtilPlus.Base64.decodeBase64(wxPayAuth.getMerchantPrivateKey()), Boolean.TRUE);
            } catch (Exception e) {
                log.error("=====unifiedAppOrder sign error", e);
            }

            UnifiedAppOrderData appOrderData = new UnifiedAppOrderData();
            appOrderData.setAppId(wxPayAuth.getWxAppId());
            appOrderData.setPartnerId(wxPayAuth.getMerchantId());
            appOrderData.setPrepayId(prepayId);
            appOrderData.setPackageValue("Sign=WXPay");
            appOrderData.setNonceStr(nonceStr);
            appOrderData.setTimeStamp(timestamp);
            appOrderData.setSign(sign);
            appOrderData.setApplyPayNo(outTradeNo);
            return appOrderData;
        }
    }

    public class Callback {

        public OrderData decryptPayCallback(String wxAppId, Map<String, String> returnHeaderMap, String returnParam, WxPayResultCb payResultCb) {
            WxPayAuth wxPayAuth = wxPayAuthRepository.choosWxPayAuth(wxAppId);
            try {
                verifyHeader(wxPayAuth, returnHeaderMap, returnParam);

                String cipherText = payResultCb.getResource().getCipherText();
                String nonce = payResultCb.getResource().getNonce();
                String associatedData = payResultCb.getResource().getAssociatedData();
                String realCallback = DigestUtilPlus.AES.decryptGCM(
                        DigestUtilPlus.Base64.decodeBase64(cipherText),
                        wxPayAuth.getMerchantAesKey().getBytes(StringUtilPlus.UTF_8),
                        nonce.getBytes(StringUtilPlus.UTF_8),
                        associatedData.getBytes(StringUtilPlus.UTF_8)
                );
                OrderData orderData = WxFormatUtils.OBJECT_MAPPER.readValue(realCallback, OrderData.class);
                if (orderData.getTradeState() == TradeState.SUCCESS) {
                    orderData.setActualPayAmount(BigDecimal.valueOf(orderData.getAmount().getPayerTotal()).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN)); //订单总金额，单位为分
                    orderData.setResponseStr("接口回调:" + realCallback);
                    return orderData;
                } else {
                    log.error("======WeixinPayV3Helper decryptPayCallback result[{}] not success:", realCallback);
                    return new OrderData();
                }
            } catch (Exception e) {
                log.error("======WeixinPayV3Helper decryptPayCallback error:", e);
                return new OrderData();
            }
        }

        /**
         * 退款回调解码
         *
         * @param payResultCb 回调密文
         * @return 解码结果
         */
        public OrderRefundData decryptRefundCallback(String wxAppId, WxPayResultCb payResultCb) {
            WxPayAuth wxPayAuth = wxPayAuthRepository.choosWxPayAuth(wxAppId);
            try {
                String cipherText = payResultCb.getResource().getCipherText();
                String nonce = payResultCb.getResource().getNonce();
                String associatedData = payResultCb.getResource().getAssociatedData();
                String realCallback = DigestUtilPlus.AES.decryptGCM(
                        DigestUtilPlus.Base64.decodeBase64(cipherText),
                        wxPayAuth.getMerchantAesKey().getBytes(StringUtilPlus.UTF_8),
                        nonce.getBytes(StringUtilPlus.UTF_8),
                        associatedData.getBytes(StringUtilPlus.UTF_8)
                );
                OrderRefundData orderRefundV3Rsp = WxFormatUtils.OBJECT_MAPPER.readValue(realCallback, OrderRefundData.class);
                if (orderRefundV3Rsp.getStatus() == RefundStatus.SUCCESS) {
                    orderRefundV3Rsp.setActualRefundAmount(BigDecimal.valueOf(orderRefundV3Rsp.getAmount().getPayerTotal()).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN)); //订单总金额，单位为分
                    orderRefundV3Rsp.setResponseStr(realCallback);
                    return orderRefundV3Rsp;
                } else {
                    log.error("======WeixinPayV3Helper decryptRefundCallback result[{}] not success:", realCallback);
                    return null;
                }
            } catch (Exception e) {
                log.error("======WeixinPayV3Helper decryptRefundCallback error:", e);
                return null;
            }
        }
    }

    /**
     * 校验头信息
     *
     * @param returnHeaderMap 头信息
     * @param returnParam     体信息
     */
    private void verifyHeader(WxPayAuth wxPayAuth, Map<String, String> returnHeaderMap, String returnParam) throws SSLException {
        String weixinSign = returnHeaderMap.get("Wechatpay-Signature");
        String weixinPemSerial = returnHeaderMap.get("Wechatpay-Serial");
        String timestamp = returnHeaderMap.get("Wechatpay-Timestamp");
        String nonce = returnHeaderMap.get("Wechatpay-Nonce");

        long currTimeStamp = System.currentTimeMillis() / 1000;
        if (currTimeStamp - 600 > Long.parseLong(timestamp) || currTimeStamp + 600 < Long.parseLong(timestamp)) {
            throw new SSLException("sign currTimeStamp verify failed!");
        }

        String waitSign = String.format("%s\n%s\n%s\n", timestamp, nonce, returnParam);
        //获取证书
        try {
            PlantCertRedisDto plantCertRedisDto = this.plantCert(wxPayAuth, weixinPemSerial);
            boolean matchResult = DigestUtilPlus.RSA256.verifySignPublicKey(waitSign.getBytes(StringUtilPlus.UTF_8), weixinSign, DigestUtilPlus.Base64.decodeBase64(plantCertRedisDto.getPublicKey()));
            if (!matchResult) {
                throw new SSLException("sign not match!");
            }
        } catch (Exception e) {
            throw new SSLException("sign verify failed!", e);
        }
    }

    private PlantCertRedisDto plantCert(WxPayAuth wxPayAuth, String weixinPemSerial) throws BusinessException {
        //如果没有证书编号且已经下载过证书了，忽略这个请求
        if (StringUtilPlus.isBlank(weixinPemSerial)) {
            Map<String, Object> redisWeixinPem = redisHelper.hGetAll(WxCache.THIRD_WX_PAY_H, wxPayAuth.getMerchantId());
            if (CollectionUtilPlus.Map.isNotEmpty(redisWeixinPem)) {
                return null;
            }
        }

        String redisKey = wxPayAuth.getMerchantId();
        return redisHelper.lockAndGet(WxCache.THIRD_WX_PAY_H, weixinPemSerial, Duration.ofSeconds(5), (locked) -> {
            if (locked) {
                List<PlantCertData> plantCertDataList = wxPayCertApiProxy.certDownload(wxPayAuth.getWxAppId()).getData();
                Map<String, Object> weixinPem = new HashMap<>();
                PlantCertRedisDto needReturn = null;
                try {
                    for (PlantCertData certData : plantCertDataList) {
                        String cipherText = certData.getEncryptCertificate().getCipherText();
                        String nonce = certData.getEncryptCertificate().getNonce();
                        String associatedData = certData.getEncryptCertificate().getAssociatedData();
                        String pem = DigestUtilPlus.AES.decryptGCM(DigestUtilPlus.Base64.decodeBase64(cipherText), wxPayAuth.getMerchantAesKey().getBytes(StringUtilPlus.UTF_8), nonce.getBytes(StringUtilPlus.UTF_8), associatedData.getBytes(StringUtilPlus.UTF_8));

                        //通过服务器证书获取服务器支付公钥
                        CertificateFactory cf = CertificateFactory.getInstance("X509");
                        X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(pem.getBytes(StringUtilPlus.UTF_8)));
                        x509Cert.checkValidity();
                        String weixinPublicKey = DigestUtilPlus.Base64.encodeBase64String(x509Cert.getPublicKey().getEncoded());

                        //存入缓存
                        PlantCertRedisDto plantCertRedisDto = new PlantCertRedisDto();
                        plantCertRedisDto.setSerialNo(certData.getSerialNo());
                        plantCertRedisDto.setEffectiveTime(certData.getEffectiveTime().toLocalDateTime());
                        plantCertRedisDto.setExpireTime(certData.getExpireTime().toLocalDateTime());
                        plantCertRedisDto.setPublicKey(weixinPublicKey);
                        weixinPem.put(certData.getSerialNo(), plantCertRedisDto);

                        if (StringUtilPlus.equals(plantCertRedisDto.getSerialNo(), weixinPemSerial)) {
                            needReturn = plantCertRedisDto;
                        }
                    }
                    redisHelper.hPutAll(WxCache.THIRD_WX_PAY_H, redisKey, weixinPem);
                    return needReturn;
                } catch (Exception e) {
                    log.error("weixin plantCert error: ", e);
                    return null;
                }
            } else {
                if (StringUtilPlus.isBlank(weixinPemSerial)) {
                    return null;
                }
                PlantCertRedisDto plantCertRedisDto = (PlantCertRedisDto) redisHelper.hGet(WxCache.THIRD_WX_PAY_H, redisKey, weixinPemSerial);
                if (plantCertRedisDto != null && LocalDateTime.now().isBefore(plantCertRedisDto.getExpireTime())) {
                    return plantCertRedisDto;
                } else {
                    return null;
                }
            }
        });
    }
}
