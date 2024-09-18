package cn.jzyunqi.common.third.weixin.mp.model.response;

import cn.jzyunqi.common.third.weixin.common.response.WeixinRsp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author wiiyaya
 * @since 2018/5/29.
 */
@Getter
@Setter
public class JsApiTicketRsp extends WeixinRsp {
    @Serial
    private static final long serialVersionUID = -3902104748545703809L;

    /**
     * ticket
     */
    private String ticket;

    /**
     * ticket过期时间
     */
    @JsonProperty("expires_in")
    private long expiresIn;
}
