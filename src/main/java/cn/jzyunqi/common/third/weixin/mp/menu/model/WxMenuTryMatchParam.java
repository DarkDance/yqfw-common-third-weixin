package cn.jzyunqi.common.third.weixin.mp.menu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMenuTryMatchParam {
    @JsonProperty("user_id")
    private String userId;
}
