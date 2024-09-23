package cn.jzyunqi.common.third.weixin.mp.menu.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @since 2018/8/22.
 */
@Getter
@Setter
public class WxMenuData extends WeixinRsp implements Serializable {
    @Serial
    private static final long serialVersionUID = -7895223754676094123L;

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
