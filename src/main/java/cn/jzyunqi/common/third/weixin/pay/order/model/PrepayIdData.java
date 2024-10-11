package cn.jzyunqi.common.third.weixin.pay.order.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2021/7/20.
 */
@Getter
@Setter
public class PrepayIdData extends WeixinRspV1 {

    /**
     * 预支付交易会话标识
     */
    private String prepayId;
}
