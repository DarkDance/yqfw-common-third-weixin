package cn.jzyunqi.common.third.weixin.mp.menu.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
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
public class WxMenuData extends WeixinRspV1{

    /**
     * 目录id
     */
    @JsonProperty("menuid")
    private String menuId;

    /**
     * 目录按钮
     */
    private List<WxMenuButtonData> button;

    /**
     * 个性化规则
     */
    @JsonProperty("matchrule")
    private WxMenuRuleData matchRule;
}
