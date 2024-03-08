package cn.jzyunqi.common.third.weixin.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.support.spring.mvc.DefaultResponseErrorHandlerPlus;
import cn.jzyunqi.common.third.weixin.enums.ResultCode;
import cn.jzyunqi.common.third.weixin.enums.ReturnCode;
import cn.jzyunqi.common.third.weixin.enums.TradeState;
import cn.jzyunqi.common.third.weixin.enums.TradeType;
import cn.jzyunqi.common.third.weixin.model.OrderQueryResult;
import cn.jzyunqi.common.third.weixin.model.OrderRefundResult;
import cn.jzyunqi.common.third.weixin.model.UnifiedOrderResult;
import cn.jzyunqi.common.third.weixin.response.OrderQueryRsp;
import cn.jzyunqi.common.third.weixin.response.OrderRefundRsp;
import cn.jzyunqi.common.third.weixin.response.UnifiedOrderRsp;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.security.KeyStore;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wiiyaya
 * @date 2018/5/29.
 */
@Slf4j
public class WeixinPayHelper {

    /**
     * 统一下单
     */
    private static final String WX_UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 查询订单
     */
    private static final String WX_ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 申请退款
     */
    private static final String WX_PAY_REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    private String merchantSecret;

    private String merchantId;

    private String refundCertFilePath;

    private String refundCertFileSecret;

    private String callbackUrl;

    private RestTemplate restTemplate;

    private RestTemplate httpsRestTemplate;

    private Map<String, String> supportAppIdMap;

    public WeixinPayHelper(String merchantId, String merchantSecret, String callbackUrl, RestTemplate restTemplate, Map<String, String> supportAppIdMap) throws Exception {
        this(merchantId, merchantSecret, callbackUrl, restTemplate, null, null, supportAppIdMap);
    }

    public WeixinPayHelper(String merchantId, String merchantSecret, String callbackUrl, RestTemplate restTemplate, String refundCertFilePath, String refundCertFileSecret, Map<String, String> supportAppIdMap) throws Exception {
        this.merchantId = merchantId;
        this.merchantSecret = merchantSecret;
        this.refundCertFilePath = refundCertFilePath;
        this.refundCertFileSecret = refundCertFileSecret;
        this.callbackUrl = callbackUrl;
        this.restTemplate = restTemplate;
        this.httpsRestTemplate = this.prepareHttpsRestTemplate(restTemplate.getMessageConverters());
        this.supportAppIdMap = supportAppIdMap;
    }

    /**
     * 微信下单
     *
     * @param outTradeNo       本地单号
     * @param simpleDesc       简单描述
     * @param clientIp         用户端实际ip
     * @param amount           订单总金额
     * @param expiresInMinutes 几分钟过期
     * @param openId           支付人微信id
     * @param creditSupport    支持信用卡
     * @return 支付签名
     */
    public UnifiedOrderResult signForPay(String appIdKey, TradeType tradeType, String outTradeNo, String simpleDesc, String clientIp, BigDecimal amount, int expiresInMinutes, String openId, boolean creditSupport) throws BusinessException {
        String appId = supportAppIdMap.get(appIdKey);
        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("appid", appId); //微信开放平台审核通过的应用APPID
        paramMap.put("body", StringUtilPlus.substring(StringUtilPlus.replaceEmoji(simpleDesc).toString(), 0, 128)); //商品或支付单简要描述
        paramMap.put("mch_id", merchantId); //微信支付分配的商户号
        String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
        paramMap.put("nonce_str", nonceStr); //随机字符串，不长于32位
        paramMap.put("notify_url", callbackUrl); //微信支付异步通知回调地址
        paramMap.put("out_trade_no", outTradeNo); //商户订单号
        paramMap.put("spbill_create_ip", clientIp); //用户端实际ip
        paramMap.put("total_fee", String.valueOf(amount.multiply(new BigDecimal(100)).intValue())); //订单总金额，单位为分
        paramMap.put("time_expire", LocalDateTime.now().plusMinutes(expiresInMinutes).format(DateTimeUtilPlus.WEIXIN_DATE_FORMAT));//订单失效时间
        paramMap.put("trade_type", tradeType.toString()); //支付类型

        if (!creditSupport) {
            paramMap.put("limit_pay", "no_credit");//不支持信用卡支付
        }

        if(tradeType == TradeType.JSAPI){
            paramMap.put("openid", openId); //支付人id
        }

        String sign = this.getSign(paramMap);
        paramMap.put("sign", sign);

        UnifiedOrderRsp unifiedOrderRsp;
        try {
            URI uri = new URIBuilder(WX_UNIFIED_ORDER_URL).build();
            RequestEntity<String> requestEntity = new RequestEntity<>(this.getRequestXml(paramMap), HttpMethod.POST, uri);
            ResponseEntity<UnifiedOrderRsp> sendRsp = restTemplate.exchange(requestEntity, UnifiedOrderRsp.class);
            unifiedOrderRsp = sendRsp.getBody();
        } catch (Exception e) {
            log.error("======WeixinPayHelper signForPay error:", e);
            throw new BusinessException("common_error_wx_unified_order_error 微信统一下单失败，失败原因:xml解析错误");
        }

        if (unifiedOrderRsp != null && unifiedOrderRsp.getReturnCode() == ReturnCode.SUCCESS) {
            if (unifiedOrderRsp.getResultCode() == ResultCode.SUCCESS) {
                Long timestamp = System.currentTimeMillis() / 1000;
                Map<String, String> appParamMap = new LinkedHashMap<>();
                UnifiedOrderResult unifiedOrderResult = new UnifiedOrderResult();
                switch (tradeType) {
                    case APP:
                        appParamMap.put("appid", appId);
                        appParamMap.put("partnerid", merchantId);
                        appParamMap.put("prepayid", unifiedOrderRsp.getPrepayId()); //微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
                        appParamMap.put("package", "Sign=WXPay");
                        appParamMap.put("noncestr", nonceStr);
                        appParamMap.put("timestamp", timestamp.toString());

                        unifiedOrderResult.setWeixinPackage("Sign=WXPay"); //扩展字段
                        break;
                    case JSAPI:
                        appParamMap.put("appId", appId);
                        appParamMap.put("timeStamp", timestamp.toString());
                        appParamMap.put("nonceStr", nonceStr);
                        appParamMap.put("package", "prepay_id=" + unifiedOrderRsp.getPrepayId()); //微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
                        appParamMap.put("signType", "MD5");

                        unifiedOrderResult.setWeixinPackage("prepay_id=" + unifiedOrderRsp.getPrepayId()); //扩展字段
                        break;
                    default:
                        break;
                }
                unifiedOrderResult.setAppId(appId); //应用ID
                unifiedOrderResult.setMerchantId(merchantId); //商户号
                unifiedOrderResult.setNonceStr(nonceStr); //随机字符串
                unifiedOrderResult.setTimestamp(timestamp); //时间戳
                unifiedOrderResult.setApplyPayNo(outTradeNo); //订单号
                unifiedOrderResult.setPrepayId(unifiedOrderRsp.getPrepayId()); //预支付交易会话ID
                unifiedOrderResult.setSign(this.getSign(appParamMap)); //签名
                return unifiedOrderResult;
            } else {
                log.error("======微信下单失败resultCode不正确,wxPayApplySignModel:{}", ToStringBuilder.reflectionToString(unifiedOrderRsp));
                throw new BusinessException("common_error_wx_unified_order_result_failed");
            }
        } else {
            log.error("======微信统一下单失败returnCode不正确,wxPayApplySignModel:{}", ToStringBuilder.reflectionToString(unifiedOrderRsp));
            throw new BusinessException("common_error_wx_unified_order_return_failed");
        }
    }

    /**
     * 根据单号查询支付结果
     *
     * @param transactionId 微信单号
     * @param outTradeNo    本地单号
     * @return 订单结果
     */
    public OrderQueryResult queryPay(String appIdKey, String transactionId, String outTradeNo) {
        String appId = supportAppIdMap.get(appIdKey);
        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("appid", appId); //微信开放平台审核通过的应用APPID
        paramMap.put("mch_id", merchantId); //微信支付分配的商户号
        String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
        paramMap.put("nonce_str", nonceStr); //随机字符串，不长于32位
        if (StringUtilPlus.isNotEmpty(transactionId)) {
            paramMap.put("transaction_id", transactionId); //微信单号
        } else {
            paramMap.put("out_trade_no", outTradeNo); //本地单号
        }
        String sign = this.getSign(paramMap);
        paramMap.put("sign", sign);

        OrderQueryRsp orderQueryRsp;
        try {
            URI uri = new URIBuilder(WX_ORDER_QUERY_URL).build();
            RequestEntity<String> requestEntity = new RequestEntity<>(this.getRequestXml(paramMap), HttpMethod.POST, uri);
            ResponseEntity<OrderQueryRsp> sendRsp = restTemplate.exchange(requestEntity, OrderQueryRsp.class);
            orderQueryRsp = sendRsp.getBody();
        } catch (Exception e) {
            log.error("======WeixinPayHelper queryPay error:", e);
            return null;
        }

        if (orderQueryRsp != null && orderQueryRsp.getReturnCode() == ReturnCode.SUCCESS && orderQueryRsp.getResultCode() == ResultCode.SUCCESS && orderQueryRsp.getTradeState() == TradeState.SUCCESS) {
            OrderQueryResult dto = new OrderQueryResult();
            dto.setOutTradeNo(orderQueryRsp.getOutTradeNo()); //商户订单号
            dto.setTransactionId(orderQueryRsp.getTransactionId()); //微信支付订单号
            dto.setTotalFee(orderQueryRsp.getTotalFee().divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN)); //订单总金额，单位为分
            dto.setResponseStr("主动请求:" + ToStringBuilder.reflectionToString(orderQueryRsp));
            return dto;
        } else {
            log.error("======WeixinPayHelper queryPay result[{}] not success:", ToStringBuilder.reflectionToString(orderQueryRsp));
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
    public OrderRefundResult payRefund(String appIdKey, String transactionId, String outRefundNo, BigDecimal totalFee, BigDecimal refundFee, String refundDesc) throws BusinessException {
        String appId = supportAppIdMap.get(appIdKey);
        Map<String, String> paramMap = new LinkedHashMap<>();
        paramMap.put("appid", appId); //微信开放平台审核通过的应用APPID
        paramMap.put("mch_id", merchantId); //微信支付分配的商户号
        String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
        paramMap.put("nonce_str", nonceStr); //随机字符串，不长于32位
        paramMap.put("transaction_id", transactionId); //微信订单号
        paramMap.put("out_refund_no", outRefundNo); //退款流水号
        String totalFeeS = String.valueOf(totalFee.multiply(new BigDecimal(100)).intValue());
        String refundFeeS = String.valueOf(refundFee.multiply(new BigDecimal(100)).intValue());
        paramMap.put("total_fee", totalFeeS); //订单金额
        paramMap.put("refund_fee", refundFeeS); //退款金额
        paramMap.put("op_user_id", merchantId); //操作员账号, 默认为商户号
        paramMap.put("refund_desc", refundDesc); //退款原因
        paramMap.put("refund_account", "REFUND_SOURCE_RECHARGE_FUNDS"); //使用余额账户退款

        String sign = this.getSign(paramMap);
        paramMap.put("sign", sign);

        OrderRefundRsp orderRefundRsp;
        try {
            URI uri = new URIBuilder(WX_PAY_REFUND_URL).build();
            RequestEntity<String> requestEntity = new RequestEntity<>(getRequestXml(paramMap), HttpMethod.POST, uri);
            ResponseEntity<OrderRefundRsp> sendRsp = httpsRestTemplate.exchange(requestEntity, OrderRefundRsp.class);
            orderRefundRsp = sendRsp.getBody();
        } catch (Exception e) {
            log.error("======WeixinPayExecutor executeRefund error:", e);
            throw new BusinessException("common_error_wx_refund_error");
        }

        if (orderRefundRsp != null && orderRefundRsp.getReturnCode() == ReturnCode.SUCCESS && orderRefundRsp.getResultCode() == ResultCode.SUCCESS) {
            OrderRefundResult dto = new OrderRefundResult();
            dto.setRefundId(orderRefundRsp.getRefundId());
            dto.setRefundFee(orderRefundRsp.getRefundFee().divide(new BigDecimal(100), 2, BigDecimal.ROUND_DOWN));
            dto.setResponseStr(ToStringBuilder.reflectionToString(orderRefundRsp));
            return dto;
        } else {
            log.error("======WeixinPayExecutor executeRefund result[{}] not success:", ToStringBuilder.reflectionToString(orderRefundRsp));
            throw new BusinessException("common_error_wx_refund_failed");
        }
    }

    /**
     * 签名
     *
     * @param paramMap      参数
     * @return 签名字符串
     */
    private String getSign(Map<String, String> paramMap) {
        String paramStr = CollectionUtilPlus.Map.getUrlParam(paramMap, true, false,false);
        paramStr = paramStr + "&key=" + merchantSecret;
        return DigestUtilPlus.MD5.sign(paramStr, Boolean.FALSE).toUpperCase();
    }

    /**
     * 组装xml
     *
     * @param paramMap 参数
     * @return xml字符串
     */
    private String getRequestXml(Map<String, String> paramMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (String key : paramMap.keySet()) {
            sb.append("<").append(key).append(">").append("<![CDATA[").append(paramMap.get(key)).append("]]></").append(key).append(">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    private RestTemplate prepareHttpsRestTemplate(List<HttpMessageConverter<?>> messageConverters) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(refundCertFilePath)) {
            keyStore.load(fis, refundCertFileSecret.toCharArray());
        }

        //SSL设置自己的证书，同时信任所有的访问服务器
        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(keyStore, refundCertFileSecret.toCharArray())
                .loadTrustMaterial((x509CertificatesChain, authType) -> true)
                .build();

        SSLConnectionSocketFactory sslFactory = SSLConnectionSocketFactoryBuilder.create()
                .setSslContext(sslContext)
                //SSL不校验主机名
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();

        SocketConfig socketConfig = SocketConfig.custom()
                //设置读取超时
                .setSoTimeout(Timeout.ofSeconds(100))
                .build();

        HttpClientConnectionManager connMgr = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslFactory)
                //整个连接池的并发
                .setMaxConnTotal(30)
                //每个远程主机的并发
                .setMaxConnPerRoute(10)
                .setDefaultSocketConfig(socketConfig)
                .build();

        //每个证书都生成对应的client
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                        .setConnectionManager(connMgr)
                        .build();

        //设置请求生成工厂
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(50000);//设置连接超时
        requestFactory.setConnectionRequestTimeout(20000);//设置连接池连接不够用的等待时长

        //创建restTemplate
        RestTemplate restTemplate = new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandlerPlus());
        return restTemplate;
    }
}
