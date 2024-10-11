package cn.jzyunqi.common.third.weixin.mp.menu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
@ToString
public class WxMpSelfMenuData {

    /**
     * 菜单按钮
     */
    private List<WxMenuButtonData> button;
}
