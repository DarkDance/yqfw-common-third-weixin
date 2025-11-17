package cn.jzyunqi.common.third.weixin.common.constant;

import cn.jzyunqi.common.feature.redis.Cache;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@AllArgsConstructor
public enum WxCache implements Cache {

    /**
     * 微信公众号缓存
     */
    THIRD_WX_MP_V(Duration.ZERO, Boolean.FALSE),

    /**
     * 企业微信缓存
     */
    THIRD_WX_CP_V(Duration.ZERO, Boolean.FALSE),

    /**
     * 微信支付缓存
     */
    THIRD_WX_PAY_H(Duration.ZERO, Boolean.FALSE);

    private final Duration expiration;

    private final Boolean autoRenew;
}
