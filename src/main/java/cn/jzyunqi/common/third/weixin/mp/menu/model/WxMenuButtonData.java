package cn.jzyunqi.common.third.weixin.mp.menu.model;

import cn.jzyunqi.common.third.weixin.mp.menu.enums.ButtonType;
import cn.jzyunqi.common.third.weixin.mp.menu.enums.ClickType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @since 2018/8/22.
 */
@Getter
@Setter
@ToString
public class WxMenuButtonData  {

    /**
     * 菜单类型
     */
    @JsonIgnore
    private ButtonType buttonType;

    /**
     * 点击类型
     */
    private ClickType type;

    /**
     * 名称
     */
    private String name;

    /**
     * URL
     */
    private String url;

    /**
     * key
     */
    private String key;

    /**
     * media_id
     */
    private String mediaId;

    /**
     * 子菜单列表
     */
    private List<WxMenuButtonData> subButton;
}
