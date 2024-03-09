package cn.jzyunqi.common.third.weixin.model.redis;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wiiyaya
 * @date 2018/5/31.
 */
@Getter
@Setter
public class JsApiTicketRedisDto implements Serializable {
    private static final long serialVersionUID = -1882849843222236844L;

    /**
     * 授权ticket
     */
    private String ticket;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
}
