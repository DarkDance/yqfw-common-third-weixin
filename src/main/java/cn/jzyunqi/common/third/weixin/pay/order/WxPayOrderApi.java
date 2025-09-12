package cn.jzyunqi.common.third.weixin.pay.order;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.enums.WeixinPaySubType;
import cn.jzyunqi.common.third.weixin.common.utils.WxFormatUtils;
import cn.jzyunqi.common.third.weixin.pay.WxPayAuth;
import cn.jzyunqi.common.third.weixin.pay.WxPayAuthHelper;
import cn.jzyunqi.common.third.weixin.pay.cert.WxPayCertApi;
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
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
@Slf4j
public class WxPayOrderApi {

    @Resource
    private WxPayAuthHelper wxPayAuthHelper;

    @Resource
    private WxPayOrderApiProxy wxPayOrderApiProxy;

    //JSAPI、小程序支付 - 预下单
    public UnifiedJsapiOrderData unifiedJsapiOrder(String wxAppId, WeixinPaySubType paySubType, String outTradeNo, String simpleDesc, BigDecimal amount, int expiresInMinutes, String openId) throws BusinessException {
        WxPayAuth wxPayAuth = wxPayAuthHelper.chooseWxPayAuth(wxAppId);
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
        WxPayAuth wxPayAuth = wxPayAuthHelper.chooseWxPayAuth(wxAppId);
        OrderData orderData;
        if (StringUtilPlus.isNotEmpty(transactionId)) {
            orderData = wxPayOrderApiProxy.queryOrderByTransactionId(wxPayAuth.getWxAppId(), transactionId, wxPayAuth.getMerchantId());
        } else {
            orderData = wxPayOrderApiProxy.queryOrderByOutTradeNo(wxPayAuth.getWxAppId(), outTradeNo, wxPayAuth.getMerchantId());
        }
        if (orderData.getTradeState() == TradeState.SUCCESS) {
            orderData.setActualPayAmount(BigDecimal.valueOf(orderData.getAmount().getPayerTotal()).divide(BigDecimal.valueOf(100), 2, RoundingMode.DOWN)); //订单总金额，单位为分
            orderData.setResponseStr("主动查询订单成功");
        } else {
            log.error("======WeixinPayV3Helper queryOrder result[{}] not success:", orderData);
            return new OrderData();
        }
        return orderData;
    }

    //小程序支付 - 退款申请
    public OrderRefundData refundApply(String wxAppId, String transactionId, String outRefundNo, BigDecimal totalFee, BigDecimal refundFee, String refundDesc) throws BusinessException {
        WxPayAuth wxPayAuth = wxPayAuthHelper.chooseWxPayAuth(wxAppId);
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
        WxPayAuth wxPayAuth = wxPayAuthHelper.chooseWxPayAuth(wxAppId);
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
        WxPayAuth wxPayAuth = wxPayAuthHelper.chooseWxPayAuth(wxAppId);
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
        WxPayAuth wxPayAuth = wxPayAuthHelper.chooseWxPayAuth(wxAppId);
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
