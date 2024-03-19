package cn.jzyunqi.common.third.weixin;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.client.WeixinPayV3Client;
import cn.jzyunqi.common.third.weixin.model.callback.PayResultCb;
import cn.jzyunqi.common.third.weixin.model.response.OrderQueryV3Rsp;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * 微信回调接口
 *
 * @author wiiyaya
 * @date 2021/5/9.
 */
public abstract class AWeixinPayCbController {

    @Resource
    private WeixinPayV3Client weixinPayV3Client;

    /**
     * 微信支付回调
     *
     * @param payResultCb 请求参数
     */
    @RequestMapping
    @ResponseBody
    public void payApplyWeixinCallback(@RequestBody PayResultCb payResultCb, @RequestBody String body, @RequestHeader HttpHeaders headers) throws BusinessException {
        if (StringUtilPlus.equalsIgnoreCase(payResultCb.getEventType(), "TRANSACTION.SUCCESS")) {
            OrderQueryV3Rsp orderQueryV3Rsp = weixinPayV3Client.decryptPayCallback(headers.toSingleValueMap(), body, payResultCb);
            if(orderQueryV3Rsp != null){
                paySuccess(orderQueryV3Rsp.getOutTradeNo(), orderQueryV3Rsp.getTransactionId(), orderQueryV3Rsp.getActualPayAmount(), orderQueryV3Rsp.getResponseStr());
            }else{
                throw new BusinessException("common_weixin_pay_call_back_failed");
            }
        } else {
            throw new BusinessException("common_weixin_pay_call_back_not_success");
        }
    }

    /**
     * 支付成功回调
     *
     * @param applyPayNo 申请支付订单号
     * @param actualPayNo 实际支付订单号
     * @param actualPayAmount 实际支付金额
     * @param returnParam 返回参数
     */
    protected abstract void paySuccess(String applyPayNo, String actualPayNo, BigDecimal actualPayAmount, String returnParam);
}
