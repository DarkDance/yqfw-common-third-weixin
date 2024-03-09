package cn.jzyunqi.common.third.weixin.model.request.item;

import cn.jzyunqi.common.third.weixin.model.enums.ButtonType;
import cn.jzyunqi.common.third.weixin.model.enums.ClickType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2018/8/22.
 */
@Getter
@Setter
public class MenuButtonData implements Serializable {
    @Serial
    private static final long serialVersionUID = -4706066149001165271L;

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
    @JsonProperty("media_id")
    private String mediaId;

    /**
     * 子菜单列表
     */
    @JsonProperty("sub_button")
    private List<MenuButtonData> subButton;
}
