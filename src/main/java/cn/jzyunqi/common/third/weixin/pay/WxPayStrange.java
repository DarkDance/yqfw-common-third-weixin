package cn.jzyunqi.common.third.weixin.pay;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.pay.PayApplyReqDto;
import cn.jzyunqi.common.feature.pay.PayCallbackDto;
import cn.jzyunqi.common.feature.pay.PayHelper;
import cn.jzyunqi.common.feature.pay.PayQueryReqDto;
import cn.jzyunqi.common.feature.pay.RefundApplyReqDto;
import cn.jzyunqi.common.feature.pay.RefundCallbackDto;
import cn.jzyunqi.common.feature.pay.VerifyPayResult;
import cn.jzyunqi.common.third.weixin.common.enums.TradeType;
import cn.jzyunqi.common.third.weixin.common.enums.WeixinPaySubType;
import cn.jzyunqi.common.third.weixin.common.enums.WeixinType;
import cn.jzyunqi.common.third.weixin.pay.callback.model.WxPayResultCb;
import cn.jzyunqi.common.third.weixin.pay.order.enums.TradeState;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderRefundData;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.annotation.Resource;

/**
 * @author wiiyaya
 * @since 2024/7/16
 */
public class WxPayStrange implements PayHelper {

    public static final String ID = "weixin";

    @Resource
    private WxPayClient wxPayClient;

    @Override
    public Object signForPay(PayApplyReqDto payApplyReqDto) throws BusinessException {
        WeixinPaySubType paySubType = WeixinPaySubType.valueOf(payApplyReqDto.getApplyPaySubType());
        return switch (paySubType) {
            case APP ->
                    wxPayClient.order.unifiedAppOrder(payApplyReqDto.getApplyPayNo(), payApplyReqDto.getSkuName(), payApplyReqDto.getApplyPayAmount(), payApplyReqDto.getExpireTime());
            case MP_NATIVE ->
                    wxPayClient.order.unifiedNativeOrder(payApplyReqDto.getApplyPayNo(), payApplyReqDto.getSkuName(), payApplyReqDto.getApplyPayAmount(), payApplyReqDto.getExpireTime());
            case MP_H5 ->
                    wxPayClient.order.unifiedH5Order(payApplyReqDto.getApplyPayNo(), payApplyReqDto.getSkuName(), payApplyReqDto.getApplyPayAmount(), payApplyReqDto.getExpireTime());
            case MP_JSAPI, MINI_APP ->
                    wxPayClient.order.unifiedJsapiOrder(paySubType, payApplyReqDto.getApplyPayNo(), payApplyReqDto.getSkuName(), payApplyReqDto.getApplyPayAmount(), payApplyReqDto.getExpireTime(), payApplyReqDto.getApplyOpenId());
        };
    }

    @Override
    public VerifyPayResult verifyPayCallback(PayCallbackDto payCallbackDto) throws BusinessException {
        WxPayResultCb payResultCb = (WxPayResultCb) payCallbackDto.getReturnParamObject();
        if (StringUtilPlus.equalsIgnoreCase(payResultCb.getEventType(), "TRANSACTION.SUCCESS")) {
            OrderData orderData = wxPayClient.cb.decryptPayCallback(payCallbackDto.getReturnHeaderMap(), payCallbackDto.getReturnParam(), payResultCb);
            if (orderData.getTradeState() == TradeState.SUCCESS) {
                payCallbackDto.setApplyPayNo(orderData.getOutTradeNo()); //申请支付单号
                payCallbackDto.setActualPayNo(orderData.getTransactionId()); //微信支付单号
                payCallbackDto.setActualPayAmount(orderData.getActualPayAmount()); //支付金额
                payCallbackDto.setReturnParam(orderData.getResponseStr());
                return VerifyPayResult.SUCCESS;
            } else {
                return VerifyPayResult.FAILED;
            }
        } else {
            return VerifyPayResult.FAILED;
        }
    }

    @Override
    public VerifyPayResult verifyRefundCallback(PayCallbackDto payCallbackDto) throws BusinessException {
        WxPayResultCb payResultCb = (WxPayResultCb) payCallbackDto.getReturnParamObject();
        //REFUND.SUCCESS：退款成功通知
        //REFUND.ABNORMAL：退款异常通知
        //REFUND.CLOSED：退款关闭通知
        if (StringUtilPlus.equalsIgnoreCase(payResultCb.getEventType(), "REFUND.SUCCESS")) {
            OrderRefundData orderRefundV3Rsp = wxPayClient.cb.decryptRefundCallback(payResultCb);
            if (orderRefundV3Rsp != null) {
                payCallbackDto.setApplyPayNo(orderRefundV3Rsp.getOutTradeNo()); //申请支付单号
                payCallbackDto.setActualPayNo(orderRefundV3Rsp.getTransactionId()); //微信支付单号
                payCallbackDto.setActualPayAmount(orderRefundV3Rsp.getActualRefundAmount()); //支付金额
                payCallbackDto.setReturnParam(orderRefundV3Rsp.getResponseStr());
                return VerifyPayResult.SUCCESS;
            } else {
                return VerifyPayResult.FAILED;
            }
        } else {
            return VerifyPayResult.FAILED;
        }
    }

    @Override
    public PayCallbackDto queryPay(PayQueryReqDto payQueryReqDto) throws BusinessException {
        OrderData orderData = wxPayClient.order.queryOrder(payQueryReqDto.getActualPayNo(), payQueryReqDto.getApplyPayNo());
        if (orderData.getTradeState() == TradeState.SUCCESS) {
            PayCallbackDto payCallbackDto = new PayCallbackDto();
            payCallbackDto.setActualPayType(WxPayStrange.ID);
            payCallbackDto.setActualPaySubType(payQueryReqDto.getActualPaySubType());
            payCallbackDto.setApplyPayNo(orderData.getOutTradeNo()); //申请支付单号
            payCallbackDto.setActualPayNo(orderData.getTransactionId()); //微信支付单号
            payCallbackDto.setActualPayAmount(orderData.getActualPayAmount()); //支付金额
            payCallbackDto.setReturnParam(orderData.getResponseStr());
            return payCallbackDto;
        } else {
            return null;
        }
    }

    @Override
    public RefundCallbackDto executeRefund(RefundApplyReqDto refundApplyReqDto) throws BusinessException {
        OrderRefundData orderRefundV3Rsp = wxPayClient.order.refundApply(
                refundApplyReqDto.getActualPayNo(),
                refundApplyReqDto.getApplyRefundNo(),
                refundApplyReqDto.getActualPayAmount(),
                refundApplyReqDto.getApplyRefundAmount(),
                refundApplyReqDto.getApplyRefundReason()
        );

        RefundCallbackDto dto = new RefundCallbackDto();
        dto.setActualRefundNo(orderRefundV3Rsp.getRefundId());
        dto.setActualRefundAmount(orderRefundV3Rsp.getActualRefundAmount());
        dto.setReturnParam(orderRefundV3Rsp.getResponseStr());
        return dto;
    }
}
