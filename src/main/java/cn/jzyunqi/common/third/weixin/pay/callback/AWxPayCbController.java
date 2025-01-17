package cn.jzyunqi.common.third.weixin.pay.callback;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.pay.PayCallbackDto;
import cn.jzyunqi.common.feature.pay.PayCallbackProcessor;
import cn.jzyunqi.common.feature.pay.PayHelper;
import cn.jzyunqi.common.third.weixin.pay.WxPayStrange;
import cn.jzyunqi.common.third.weixin.pay.callback.model.WxPayResultCb;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public abstract class AWxPayCbController implements PayCallbackProcessor {

    @Resource
    @Qualifier(WxPayStrange.ID)
    private PayHelper wxPayStrange;

    /**
     * 微信支付回调
     *
     * @param payResultCb 请求参数
     */
    @RequestMapping
    @ResponseBody
    public void payApplyWeixinCallback(@RequestBody WxPayResultCb payResultCb, @RequestBody String body, @RequestHeader HttpHeaders headers) throws BusinessException {
        log.debug("""

                        ======Request Header    : {}
                        ======Request BodyStr   : {}
                        """,
                headers,
                body
        );
        //组装回调对象
        PayCallbackDto payCallbackDto = new PayCallbackDto();
        payCallbackDto.setApplyPayNo(null); //支付申请单号
        payCallbackDto.setActualPayType(WxPayStrange.ID); //实际支付方式
        payCallbackDto.setActualPaySubType(null); //这里不知道是哪个子支付
        payCallbackDto.setActualPayNo(null); //实际支付单号
        payCallbackDto.setActualPayAmount(null); //实际支付金额
        payCallbackDto.setReturnHeaderMap(headers.toSingleValueMap());//支付接口返回header
        payCallbackDto.setReturnParamMap(null); //支付接口返回的参数
        payCallbackDto.setReturnParam(body); //支付接口返回的参数JSON字符串
        payCallbackDto.setReturnParamObject(payResultCb); //支付接口返回的参数对象

        verifyPayCallback(wxPayStrange, payCallbackDto);
    }
}
