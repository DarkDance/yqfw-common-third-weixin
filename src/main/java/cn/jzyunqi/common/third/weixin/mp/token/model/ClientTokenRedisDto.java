package cn.jzyunqi.common.third.weixin.mp.token.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wiiyaya
 * @since 2018/5/31.
 */
@Getter
@Setter
@ToString
public class ClientTokenRedisDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -3361217292124952710L;

    /**
     * 授权token
     */
    private String token;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
}
