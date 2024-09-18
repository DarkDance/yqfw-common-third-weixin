package cn.jzyunqi.common.third.weixin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2018/6/26.
 */
@Getter
@AllArgsConstructor
public enum WeixinType {
    /**
     * 开放平台
     */
    OPEN(TradeType.APP),

    /**
     * 公众平台
     */
    MP(TradeType.JSAPI),

    /**
     * 小程序
     */
    MINI_APP(TradeType.JSAPI),
    ;

    private final TradeType tradeType;
}
