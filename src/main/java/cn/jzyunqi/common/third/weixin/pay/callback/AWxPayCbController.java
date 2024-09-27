package cn.jzyunqi.common.third.weixin.pay.callback;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.pay.PayCallbackDto;
import cn.jzyunqi.common.feature.pay.PayCallbackProcessor;
import cn.jzyunqi.common.feature.pay.PayHelper;
import cn.jzyunqi.common.third.weixin.pay.callback.model.PayResultCb;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 微信回调接口
 *
 * @author wiiyaya
 * @since 2021/5/9.
 */
public abstract class AWxPayCbController implements PayCallbackProcessor {

    @Resource
    @Qualifier("wxPayStrange")
    private PayHelper wxPayStrange;

    /**
     * 微信支付回调
     *
     * @param payResultCb 请求参数
     */
    @RequestMapping
    @ResponseBody
    public void payApplyWeixinCallback(@RequestBody PayResultCb payResultCb, @RequestBody String body, @RequestHeader HttpHeaders headers) throws BusinessException {
        //组装回调对象
        PayCallbackDto payCallbackDto = new PayCallbackDto();
        payCallbackDto.setApplyPayNo(null); //支付申请单号
        payCallbackDto.setActualPayType("weixin"); //实际支付方式
        payCallbackDto.setActualPayNo(null); //实际支付单号
        payCallbackDto.setActualPayAmount(null); //实际支付金额
        payCallbackDto.setReturnHeaderMap(headers.toSingleValueMap());//支付接口返回header
        payCallbackDto.setReturnParamMap(null); //支付接口返回的参数
        payCallbackDto.setReturnParam(body); //支付接口返回的参数JSON字符串
        payCallbackDto.setReturnParamObject(payResultCb); //支付接口返回的参数对象

        verifyPayCallback(wxPayStrange, payCallbackDto);
    }
}
