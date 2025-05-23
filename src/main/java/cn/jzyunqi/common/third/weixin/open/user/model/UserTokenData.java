package cn.jzyunqi.common.third.weixin.open.user.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author wiiyaya
 * @since 2018/5/22.
 */
@Getter
@Setter
@ToString
public class UserTokenData extends WeixinRspV1 {

    /**
     * 微信unionId
     */
    @JsonProperty("unionid")
    private String unionId;

    /**
     * 微信openId
     */
    @JsonProperty("openid")
    private String openId;

    /**
     * 授权token
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 刷新token
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    /**
     * 授权token过期时间
     */
    @JsonProperty("expires_in")
    private Long expiresIn;

    /**
     * 用户授权的作用域
     */
    private String scope;

    //兼容小程序用
    public String getSession_key() {
        return accessToken;
    }

    public void setSession_key(String accessToken) {
        this.accessToken = accessToken;
    }
}
