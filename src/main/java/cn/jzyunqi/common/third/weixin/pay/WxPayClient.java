package cn.jzyunqi.common.third.weixin.pay;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.weixin.common.constant.WxCache;
import cn.jzyunqi.common.third.weixin.pay.callback.model.PayResultCb;
import cn.jzyunqi.common.third.weixin.pay.cert.WxPayCertApiProxy;
import cn.jzyunqi.common.third.weixin.pay.cert.model.PlantCertData;
import cn.jzyunqi.common.third.weixin.pay.cert.model.PlantCertRedisDto;
import cn.jzyunqi.common.third.weixin.pay.order.WxPayOrderApiProxy;
import cn.jzyunqi.common.third.weixin.pay.order.enums.RefundStatus;
import cn.jzyunqi.common.third.weixin.pay.order.enums.TradeState;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderRefundData;
import cn.jzyunqi.common.third.weixin.pay.order.model.PrepayIdData;
import cn.jzyunqi.common.third.weixin.pay.order.model.RefundOrderParam;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedOrderParam;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedOrderV3Rsp;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
    private WxPayClientConfig wxPayClientConfig;

    @Resource
    private WxPayOrderApiProxy wxPayOrderApiProxy;

    @Resource
    private WxPayCertApiProxy wxPayCertApiProxy;

    @Resource
    private RedisHelper redisHelper;

    @Resource
    private ObjectMapper objectMapper;

    public final Order order = new Order();
    public final Callback cb = new Callback();

    @PostConstruct
    public void plantCertInit() throws BusinessException {
        //构造完成立即调用一次
        this.plantCert(null);
    }

    public class Order {

        public UnifiedOrderV3Rsp signForPay(String outTradeNo, String simpleDesc, BigDecimal amount, int expiresInMinutes, String openId) throws BusinessException {
            UnifiedOrderParam unifiedOrderParam = new UnifiedOrderParam();
            unifiedOrderParam.setAppId(wxPayClientConfig.getAppId());
            unifiedOrderParam.setMchId(wxPayClientConfig.getAppId());
            unifiedOrderParam.setDescription(StringUtilPlus.substring(StringUtilPlus.replaceEmoji(simpleDesc).toString(), 0, 128));
            unifiedOrderParam.setOutTradeNo(outTradeNo);
            unifiedOrderParam.setTimeExpire(OffsetDateTime.now(DateTimeUtilPlus.CHINA_ZONE_ID).plusMinutes(expiresInMinutes));
            unifiedOrderParam.setNotifyUrl(wxPayClientConfig.getPayCallbackUrl());
            unifiedOrderParam.getAmount().setTotal(amount.multiply(new BigDecimal(100)).intValue());
            unifiedOrderParam.getPayer().setOpenId(openId);

            PrepayIdData prepayIdData = wxPayOrderApiProxy.unifiedOrder(unifiedOrderParam);
            String pkg = "prepay_id=" + prepayIdData.getPrepayId();

            String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
            Long timestamp = System.currentTimeMillis() / 1000;
            String needSignContent = StringUtilPlus.join(
                    wxPayClientConfig.getAppId(), StringUtilPlus.ENTER,
                    timestamp, StringUtilPlus.ENTER,
                    nonceStr, StringUtilPlus.ENTER,
                    pkg, StringUtilPlus.ENTER
            );
            String sign = null;
            try {
                sign = DigestUtilPlus.RSA256.signPrivateKey(needSignContent.getBytes(StringUtilPlus.UTF_8), DigestUtilPlus.Base64.decodeBase64(wxPayClientConfig.getMerchantPrivateKey()), Boolean.TRUE);
            } catch (Exception e) {
                log.error("=====sign error", e);
            }

            UnifiedOrderV3Rsp unifiedOrderV3Rsp = new UnifiedOrderV3Rsp();
            unifiedOrderV3Rsp.setNonceStr(nonceStr);
            unifiedOrderV3Rsp.setTimeStamp(timestamp.toString());
            unifiedOrderV3Rsp.setWeixinPackage(pkg);
            unifiedOrderV3Rsp.setSignType("RSA");
            unifiedOrderV3Rsp.setPaySign(sign);
            unifiedOrderV3Rsp.setApplyPayNo(outTradeNo);
            return unifiedOrderV3Rsp;
        }

        //小程序支付 - 订单查询
        public OrderData queryOrder(String transactionId, String outTradeNo) throws BusinessException {
            OrderData orderData;
            if (StringUtilPlus.isNotEmpty(transactionId)) {
                orderData = wxPayOrderApiProxy.queryOrderByTransactionId(transactionId, wxPayClientConfig.getMerchantId());
            } else {
                orderData = wxPayOrderApiProxy.queryOrderByOutTradeNo(outTradeNo, wxPayClientConfig.getMerchantId());
            }
            return orderData;
        }

        //小程序支付 - 退款申请
        public OrderRefundData refundApply(String transactionId, String outRefundNo, BigDecimal totalFee, BigDecimal refundFee, String refundDesc) throws BusinessException {
            RefundOrderParam refundOrderParam = new RefundOrderParam();
            refundOrderParam.setTransactionId(transactionId);
            refundOrderParam.setOutRefundNo(outRefundNo);
            refundOrderParam.setReason(refundDesc);
            refundOrderParam.setNotifyUrl(wxPayClientConfig.getRefundCallbackUrl());
            refundOrderParam.getAmount().setRefund(refundFee.multiply(new BigDecimal(100)).intValue());
            refundOrderParam.getAmount().setTotal(totalFee.multiply(new BigDecimal(100)).intValue());
            refundOrderParam.getAmount().setCurrency("CNY");

            OrderRefundData orderRefundData = wxPayOrderApiProxy.refundApply(refundOrderParam);
            orderRefundData.setActualRefundAmount(BigDecimal.valueOf(orderRefundData.getAmount().getPayerRefund()).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN));
            try {
                orderRefundData.setResponseStr(objectMapper.writeValueAsString(orderRefundData));
            } catch (JsonProcessingException e) {
                log.warn("=====refundApply orderRefundData parse error", e);
            }
            return orderRefundData;
        }
    }

    public class Callback {

        public OrderData decryptPayCallback(Map<String, String> returnHeaderMap, String returnParam, PayResultCb payResultCb) {
            try {
                verifyHeader(returnHeaderMap, returnParam);

                String cipherText = payResultCb.getResource().getCipherText();
                String nonce = payResultCb.getResource().getNonce();
                String associatedData = payResultCb.getResource().getAssociatedData();
                String realCallback = DigestUtilPlus.AES.decryptGCM(
                        DigestUtilPlus.Base64.decodeBase64(cipherText),
                        wxPayClientConfig.getMerchantAesKey().getBytes(StringUtilPlus.UTF_8),
                        nonce.getBytes(StringUtilPlus.UTF_8),
                        associatedData.getBytes(StringUtilPlus.UTF_8)
                );
                OrderData orderQueryV3Rsp = objectMapper.readValue(realCallback, OrderData.class);
                if (orderQueryV3Rsp.getTradeState() == TradeState.SUCCESS) {
                    orderQueryV3Rsp.setActualPayAmount(BigDecimal.valueOf(orderQueryV3Rsp.getAmount().getPayerTotal()).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN)); //订单总金额，单位为分
                    orderQueryV3Rsp.setResponseStr("接口回调:" + objectMapper.writeValueAsString(orderQueryV3Rsp));
                    return orderQueryV3Rsp;
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
         * 退款回调解码
         *
         * @param payResultCb 回调密文
         * @return 解码结果
         */
        public OrderRefundData decryptRefundCallback(PayResultCb payResultCb) {
            try {
                String cipherText = payResultCb.getResource().getCipherText();
                String nonce = payResultCb.getResource().getNonce();
                String associatedData = payResultCb.getResource().getAssociatedData();
                String realCallback = DigestUtilPlus.AES.decryptGCM(
                        DigestUtilPlus.Base64.decodeBase64(cipherText),
                        wxPayClientConfig.getMerchantAesKey().getBytes(StringUtilPlus.UTF_8),
                        nonce.getBytes(StringUtilPlus.UTF_8),
                        associatedData.getBytes(StringUtilPlus.UTF_8)
                );
                OrderRefundData orderRefundV3Rsp = objectMapper.readValue(realCallback, OrderRefundData.class);
                if (orderRefundV3Rsp.getStatus() == RefundStatus.SUCCESS) {
                    orderRefundV3Rsp.setActualRefundAmount(BigDecimal.valueOf(orderRefundV3Rsp.getAmount().getPayerTotal()).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN)); //订单总金额，单位为分
                    orderRefundV3Rsp.setResponseStr(objectMapper.writeValueAsString(orderRefundV3Rsp));
                    return orderRefundV3Rsp;
                } else {
                    log.error("======WeixinPayV3Helper decryptRefundCallback result[{}] not success:", objectMapper.writeValueAsString(orderRefundV3Rsp));
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
    private void verifyHeader(Map<String, String> returnHeaderMap, String returnParam) throws SSLException {
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
            PlantCertRedisDto plantCertRedisDto = this.plantCert(weixinPemSerial);
            boolean matchResult = DigestUtilPlus.RSA256.verifySignPublicKey(waitSign.getBytes(StringUtilPlus.UTF_8), weixinSign, DigestUtilPlus.Base64.decodeBase64(plantCertRedisDto.getPublicKey()));
            if (!matchResult) {
                throw new SSLException("sign not match!");
            }
        } catch (Exception e) {
            throw new SSLException("sign verify failed!", e);
        }
    }

    private PlantCertRedisDto plantCert(String weixinPemSerial) throws BusinessException {
        Map<String, Object> weixinPem = redisHelper.hGetAll(WxCache.WX_PAY_V, wxPayClientConfig.getMerchantId());
        if (CollectionUtilPlus.Map.isNotEmpty(weixinPem) && StringUtilPlus.isBlank(weixinPemSerial)) {
            return null;
        }
        PlantCertRedisDto plantCertRedisDto = switchNewestPem(weixinPem, weixinPemSerial);
        if (plantCertRedisDto != null && LocalDateTime.now().isBefore(plantCertRedisDto.getExpireTime())) {
            return plantCertRedisDto;
        }
        List<PlantCertData> plantCertDataList = wxPayCertApiProxy.certDownload().getData();
        weixinPem = new HashMap<>();
        try {
            for (PlantCertData certData : plantCertDataList) {
                String cipherText = certData.getEncryptCertificate().getCipherText();
                String nonce = certData.getEncryptCertificate().getNonce();
                String associatedData = certData.getEncryptCertificate().getAssociatedData();
                String pem = DigestUtilPlus.AES.decryptGCM(
                        DigestUtilPlus.Base64.decodeBase64(cipherText),
                        wxPayClientConfig.getMerchantAesKey().getBytes(StringUtilPlus.UTF_8),
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
        } catch (Exception e) {
            throw new BusinessException(e, "common_error_wx_plant_cert_error", "-1", "readValue error");
        }
        redisHelper.hPutAll(WxCache.WX_PAY_V, wxPayClientConfig.getMerchantId(), weixinPem);
        return switchNewestPem(weixinPem, weixinPemSerial);
    }

    private PlantCertRedisDto switchNewestPem(Map<String, Object> weixinPem, String weixinPemSerial) {
        return weixinPem != null ? weixinPem.values().stream()
                .map(o -> (PlantCertRedisDto) o)
                .filter(plantCertRedisDto -> StringUtilPlus.equals(plantCertRedisDto.getSerialNo(), weixinPemSerial))
                .findFirst()
                .orElse(null) : null;
    }
}
