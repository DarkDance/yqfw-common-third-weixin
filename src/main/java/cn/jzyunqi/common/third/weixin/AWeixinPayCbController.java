package cn.jzyunqi.common.third.weixin;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.pay.PayCallbackDto;
import cn.jzyunqi.common.feature.pay.VerifyPayResult;
import cn.jzyunqi.common.third.weixin.model.callback.PayResultCb;
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
 * @since 2021/5/9.
 */
public abstract class AWeixinPayCbController {

    @Resource
    private WeixinPayStrange weixinPayStrange;

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
        payCallbackDto.setActualPayType(null); //实际支付方式
        payCallbackDto.setActualPayNo(null); //实际支付单号
        payCallbackDto.setActualPayAmount(null); //实际支付金额
        payCallbackDto.setReturnHeaderMap(headers.toSingleValueMap());//支付接口返回header
        payCallbackDto.setReturnParamMap(null); //支付接口返回的参数
        payCallbackDto.setReturnParam(body); //支付接口返回的参数JSON字符串
        payCallbackDto.setReturnParamObject(payResultCb); //支付接口返回的参数对象

        VerifyPayResult result = weixinPayStrange.verifyPayCallback(payCallbackDto);
        switch (result) {
            case SUCCESS:
                this.paySuccess(payCallbackDto.getApplyPayNo(), payCallbackDto.getActualPayNo(), payCallbackDto.getActualPayAmount(), payCallbackDto.getReturnParam());
                break;
            case IGNORE:
                break;
            case FAILED:
                throw new BusinessException("common_weixin_pay_call_back_failed", payCallbackDto.getActualPayType());
            default:
                break;
        }
    }

    /**
     * 支付成功回调
     *
     * @param applyPayNo      申请支付订单号
     * @param actualPayNo     实际支付订单号
     * @param actualPayAmount 实际支付金额
     * @param returnParam     返回参数
     */
    protected abstract void paySuccess(String applyPayNo, String actualPayNo, BigDecimal actualPayAmount, String returnParam);
}
