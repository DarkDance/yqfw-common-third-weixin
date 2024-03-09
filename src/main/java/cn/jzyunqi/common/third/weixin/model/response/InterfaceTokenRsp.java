package cn.jzyunqi.common.third.weixin.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author wiiyaya
 * @date 2018/5/29.
 */
@Getter
@Setter
public class InterfaceTokenRsp extends WeixinOpenRsp {
    @Serial
    private static final long serialVersionUID = 1783807609363500904L;

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
