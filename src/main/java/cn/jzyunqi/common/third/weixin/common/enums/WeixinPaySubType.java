package cn.jzyunqi.common.third.weixin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2024/10/13
 */
@Getter
@AllArgsConstructor
public enum WeixinPaySubType {

    APP(WeixinType.OPEN, TradeType.APP),
    MP_JSAPI(WeixinType.MP, TradeType.JSAPI),
    MP_NATIVE(WeixinType.MP, TradeType.NATIVE),
    MP_H5(WeixinType.MP, TradeType.MWEB),
    MINI_APP(WeixinType.MP, TradeType.JSAPI),
    ;
    private final WeixinType weixinType;
    private final TradeType tradeType;
}
