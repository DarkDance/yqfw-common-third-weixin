package cn.jzyunqi.common.third.weixin;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.pay.OrderQueryResult;
import cn.jzyunqi.common.third.weixin.client.WeixinPayV3Client;
import cn.jzyunqi.common.third.weixin.model.callback.PayResultCb;
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

    private static final String NOT_SUPPORT = "Not support now!";

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
        OrderQueryResult orderQueryResult = weixinPayV3Client.decryptPayCallback(headers.toSingleValueMap(), body, payResultCb);
        processWeixinPayCallback(orderQueryResult);
    }

    protected abstract void processWeixinPayCallback(OrderQueryResult orderQueryResult);
}
