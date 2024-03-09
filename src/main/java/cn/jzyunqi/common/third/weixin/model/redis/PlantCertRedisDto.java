package cn.jzyunqi.common.third.weixin.model.redis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wiiyaya
 * @date 2021/7/20.
 */
@Getter
@Setter
@ToString
public class PlantCertRedisDto implements Serializable {
    private static final long serialVersionUID = -2861529074190350063L;

    /**
     * 证书序列号
     */
    private String serialNo;

    /**
     * 证书有效时间
     */
    private LocalDateTime effectiveTime;

    /**
     * 证书失效时间
     */
    private LocalDateTime expireTime;

    /**
     * 证书公钥
     */
    private String publicKey;
}
