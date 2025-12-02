package cn.jzyunqi.common.third.weixin.pay.callback;

import cn.jzyunqi.common.third.weixin.common.utils.WxFormatUtils;
import cn.jzyunqi.common.third.weixin.pay.WxPayAuth;
import cn.jzyunqi.common.third.weixin.pay.WxPayAuthHelper;
import cn.jzyunqi.common.third.weixin.pay.callback.model.WxPayResultCb;
import cn.jzyunqi.common.third.weixin.pay.cert.WxPayCertApi;
import cn.jzyunqi.common.third.weixin.pay.cert.model.PlantCertRedisDto;
import cn.jzyunqi.common.third.weixin.pay.order.enums.RefundStatus;
import cn.jzyunqi.common.third.weixin.pay.order.enums.TradeState;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderRefundData;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
@Slf4j
public class WxPayCbHelper {

    @Resource
    private WxPayCertApi wxPayCertApi;

    @Resource
    private WxPayAuthHelper wxPayAuthHelper;

    public OrderData decryptPayCallback(String wxAppId, Map<String, String> returnHeaderMap, String returnParam, WxPayResultCb payResultCb) {
        WxPayAuth wxPayAuth = wxPayAuthHelper.chooseWxPayAuth(wxAppId);
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
        WxPayAuth wxPayAuth = wxPayAuthHelper.chooseWxPayAuth(wxAppId);
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
            PlantCertRedisDto plantCertRedisDto = wxPayCertApi.plantCert(wxPayAuth, weixinPemSerial);
            boolean matchResult = DigestUtilPlus.RSA.verifyWithSHA256(waitSign.getBytes(StringUtilPlus.UTF_8), weixinSign, DigestUtilPlus.Base64.decodeBase64(plantCertRedisDto.getPublicKey()));
            if (!matchResult) {
                throw new SSLException("sign not match!");
            }
        } catch (Exception e) {
            throw new SSLException("sign verify failed!", e);
        }
    }
}
