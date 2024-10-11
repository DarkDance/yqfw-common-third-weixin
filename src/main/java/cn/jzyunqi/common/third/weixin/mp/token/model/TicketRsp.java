package cn.jzyunqi.common.third.weixin.mp.token.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author wiiyaya
 * @since 2018/5/29.
 */
@Getter
@Setter
@ToString
public class TicketRsp extends WeixinRspV1 {

    /**
     * ticket
     */
    private String ticket;

    /**
     * ticket过期时间
     */
    private long expiresIn;
}
