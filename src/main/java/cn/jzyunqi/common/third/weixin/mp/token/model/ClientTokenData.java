package cn.jzyunqi.common.third.weixin.mp.token.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class ClientTokenData extends WeixinRspV1 {

    /**
     * 授权token
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 授权token过期时间(秒)
     */
    @JsonProperty("expires_in")
    private long expiresIn;

}
