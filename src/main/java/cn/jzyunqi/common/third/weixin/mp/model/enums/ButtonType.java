package cn.jzyunqi.common.third.weixin.mp.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2018/8/22.
 */
@Getter
@AllArgsConstructor
public enum ButtonType {
    /**
     * 按钮
     */
    BUTTON("按钮"),

    /**
     * 子菜单
     */
    SUB_MENU("子菜单"),
    ;

    /**
     * 描述
     */
    private String desc;
}
