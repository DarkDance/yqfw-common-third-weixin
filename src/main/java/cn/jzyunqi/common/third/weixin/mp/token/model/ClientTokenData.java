package cn.jzyunqi.common.third.weixin.mp.token.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
@ToString
public class ClientTokenData extends WeixinRspV1 {

    /**
     * 授权token
     */
    private String accessToken;

    /**
     * 授权token过期时间(秒)
     */
    private long expiresIn;

}
