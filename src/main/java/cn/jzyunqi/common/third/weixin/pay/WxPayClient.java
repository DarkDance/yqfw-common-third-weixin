package cn.jzyunqi.common.third.weixin.pay;

import cn.jzyunqi.common.third.weixin.pay.callback.WxPayCbHelper;
import cn.jzyunqi.common.third.weixin.pay.order.WxPayOrderApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@Slf4j
public class WxPayClient {

    @Resource
    public WxPayOrderApi order;

    @Resource
    public WxPayCbHelper cb;
}
