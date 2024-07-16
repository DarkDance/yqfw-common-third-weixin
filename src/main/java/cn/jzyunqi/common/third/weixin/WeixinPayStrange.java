package cn.jzyunqi.common.third.weixin;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.pay.PayApplyReqDto;
import cn.jzyunqi.common.feature.pay.PayCallbackDto;
import cn.jzyunqi.common.feature.pay.PayHelper;
import cn.jzyunqi.common.feature.pay.PayQueryReqDto;
import cn.jzyunqi.common.feature.pay.RefundApplyReqDto;
import cn.jzyunqi.common.feature.pay.RefundCallbackDto;
import cn.jzyunqi.common.feature.pay.VerifyPayResult;
import cn.jzyunqi.common.third.weixin.client.WeixinPayV3Client;
import cn.jzyunqi.common.third.weixin.model.callback.PayResultCb;
import cn.jzyunqi.common.third.weixin.model.response.OrderQueryV3Rsp;
import cn.jzyunqi.common.third.weixin.model.response.OrderRefundV3Rsp;
import cn.jzyunqi.common.utils.StringUtilPlus;

/**
 * @author wiiyaya
 * @since 2024/7/16
 */
public class WeixinPayStrange implements PayHelper {

    private final WeixinPayV3Client weixinPayV3Client;

    public WeixinPayStrange(WeixinPayV3Client weixinPayV3Client) {
        this.weixinPayV3Client = weixinPayV3Client;
    }

    @Override
    public Object signForPay(PayApplyReqDto payApplyReqDto) throws BusinessException {
        return weixinPayV3Client.signForPay(
                payApplyReqDto.getApplyPayNo(),
                payApplyReqDto.getSkuName(),
                payApplyReqDto.getApplyPayAmount(),
                payApplyReqDto.getExpireTime(),
                payApplyReqDto.getApplyOpenId()
        );
    }

    @Override
    public VerifyPayResult verifyPayCallback(PayCallbackDto payCallbackDto) throws BusinessException {
        PayResultCb payResultCb = (PayResultCb) payCallbackDto.getReturnParamObject();
        if (StringUtilPlus.equalsIgnoreCase(payResultCb.getEventType(), "TRANSACTION.SUCCESS")) {
            OrderQueryV3Rsp orderQueryV3Rsp = weixinPayV3Client.decryptPayCallback(payCallbackDto.getReturnHeaderMap(), payCallbackDto.getReturnParam(), payResultCb);
            if(orderQueryV3Rsp != null){
                payCallbackDto.setApplyPayNo(orderQueryV3Rsp.getOutTradeNo()); //申请支付单号
                payCallbackDto.setActualPayNo(orderQueryV3Rsp.getTransactionId()); //微信支付单号
                payCallbackDto.setActualPayAmount(orderQueryV3Rsp.getActualPayAmount()); //支付金额
                payCallbackDto.setReturnParam(orderQueryV3Rsp.getResponseStr());
                return VerifyPayResult.SUCCESS;
            }else{
                return VerifyPayResult.FAILED;
            }
        } else {
            return VerifyPayResult.FAILED;
        }
    }

    @Override
    public VerifyPayResult verifyRefundCallback(PayCallbackDto payCallbackDto) throws BusinessException {
        PayResultCb payResultCb = (PayResultCb) payCallbackDto.getReturnParamObject();
        //REFUND.SUCCESS：退款成功通知
        //REFUND.ABNORMAL：退款异常通知
        //REFUND.CLOSED：退款关闭通知
        if (StringUtilPlus.equalsIgnoreCase(payResultCb.getEventType(), "REFUND.SUCCESS")) {
            OrderRefundV3Rsp orderRefundV3Rsp = weixinPayV3Client.decryptRefundCallback(payResultCb);
            if(orderRefundV3Rsp != null){
                payCallbackDto.setApplyPayNo(orderRefundV3Rsp.getOutTradeNo()); //申请支付单号
                payCallbackDto.setActualPayNo(orderRefundV3Rsp.getTransactionId()); //微信支付单号
                payCallbackDto.setActualPayAmount(orderRefundV3Rsp.getActualRefundAmount()); //支付金额
                payCallbackDto.setReturnParam(orderRefundV3Rsp.getResponseStr());
                return VerifyPayResult.SUCCESS;
            }else{
                return VerifyPayResult.FAILED;
            }
        } else {
            return VerifyPayResult.FAILED;
        }
    }

    @Override
    public PayCallbackDto queryPay(PayQueryReqDto payQueryReqDto) throws BusinessException {
        OrderQueryV3Rsp orderQueryV3Rsp = weixinPayV3Client.queryPay(payQueryReqDto.getActualPayNo(), payQueryReqDto.getApplyPayNo());
        if (orderQueryV3Rsp != null) {
            PayCallbackDto payCallbackDto = new PayCallbackDto();
            payCallbackDto.setActualPayType("weixin");
            payCallbackDto.setApplyPayNo(orderQueryV3Rsp.getOutTradeNo()); //申请支付单号
            payCallbackDto.setActualPayNo(orderQueryV3Rsp.getTransactionId()); //微信支付单号
            payCallbackDto.setActualPayAmount(orderQueryV3Rsp.getActualPayAmount()); //支付金额
            payCallbackDto.setReturnParam(orderQueryV3Rsp.getResponseStr());
            return payCallbackDto;
        } else {
            return null;
        }
    }

    @Override
    public RefundCallbackDto executeRefund(RefundApplyReqDto refundApplyReqDto) throws BusinessException {
        OrderRefundV3Rsp orderRefundV3Rsp = weixinPayV3Client.payRefund(
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
