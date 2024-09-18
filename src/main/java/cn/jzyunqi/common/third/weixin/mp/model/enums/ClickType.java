package cn.jzyunqi.common.third.weixin.mp.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2018/8/22.
 */
@Getter
@AllArgsConstructor
public enum ClickType {
    /**
     * 点击事件
     */
    click("点击事件"),

    /**
     * 跳转URL
     */
    view("跳转URL"),

    /**
     * 下发消息
     */
    media_id("下发消息"),

    /**
     * 跳转图文消息URL
     */
    view_limited("跳转图文消息URL"),
    ;

    /**
     * 描述
     */
    private String desc;
}
