package cn.jzyunqi.common.third.weixin.request;

import cn.jzyunqi.common.third.weixin.model.MatchRuleData;
import cn.jzyunqi.common.third.weixin.model.MenuButtonData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2018/8/22.
 */
@Getter
@Setter
public class MenuParam implements Serializable {
    private static final long serialVersionUID = -7895223754676094123L;

    /**
     * 目录id
     */
    @JsonProperty("menuid")
    private String menuId;

    /**
     * 目录按钮
     */
    private List<MenuButtonData> button;

    /**
     * 个性化规则
     */
    @JsonProperty("matchrule")
    private MatchRuleData matchRule;
}
