package cn.jzyunqi.common.third.weixin.mp.token.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@AllArgsConstructor
public enum TicketType {
    /**
     * jsapi
     */
    JSAPI("jsapi"),
    /**
     * sdk
     */
    SDK("2"),
    /**
     * 微信卡券
     */
    WX_CARD("wx_card");

    /**
     * type代码
     */
    private final String code;
}
