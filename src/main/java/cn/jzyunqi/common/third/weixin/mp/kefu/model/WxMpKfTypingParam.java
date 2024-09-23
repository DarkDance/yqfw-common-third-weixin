package cn.jzyunqi.common.third.weixin.mp.kefu.model;

import cn.jzyunqi.common.third.weixin.mp.kefu.enums.TypingType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/20
 */
@Getter
@Setter
public class WxMpKfTypingParam {
    /**
     * 普通用户（openid）
     */
    @JsonProperty("touser")
    private String toUser;

    /**
     * 状态
     */
    private TypingType command;
}
