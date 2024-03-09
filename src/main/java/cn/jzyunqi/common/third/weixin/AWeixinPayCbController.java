package cn.jzyunqi.common.third.weixin;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.pay.OrderQueryResult;
import cn.jzyunqi.common.feature.pay.PayCbService;
import cn.jzyunqi.common.third.weixin.client.WeixinPayV3Client;
import cn.jzyunqi.common.third.weixin.model.callback.PayResultCb;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信回调接口
 *
 * @author wiiyaya
 * @date 2021/5/9.
 */
public abstract class AWeixinPayCbController {

    @Resource
    private WeixinPayV3Client weixinPayV3Client;

    @Resource
    private PayCbService payCbService;

    /**
     * 微信支付回调
     *
     * @param payResultCb 请求参数
     */
    @RequestMapping
    @ResponseBody
    public void payApplyWeixinCallback(@RequestBody PayResultCb payResultCb, @RequestBody String body, @RequestHeader HttpHeaders headers) throws BusinessException {
        if (StringUtilPlus.equalsIgnoreCase(payResultCb.getEventType(), "TRANSACTION.SUCCESS")) {
            OrderQueryResult orderQueryResult = weixinPayV3Client.decryptPayCallback(headers.toSingleValueMap(), body, payResultCb);
            if(orderQueryResult != null){
                payCbService.paySuccess("weixin_pay", orderQueryResult.getOutTradeNo(), orderQueryResult.getTransactionId(), orderQueryResult.getTotalFee(), orderQueryResult.getResponseStr());
            }else{
                throw new BusinessException("common_weixin_pay_call_back_failed");
            }
        } else {
            throw new BusinessException("common_weixin_pay_call_back_failed");
        }
    }
}
