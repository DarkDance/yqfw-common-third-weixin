package cn.jzyunqi.common.third.weixin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @date 2018/6/26.
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
    PUBLIC(TradeType.JSAPI),

    /**
     * 小程序
     */
    MP(TradeType.JSAPI),
    ;

    private TradeType tradeType;
}
