package cn.jzyunqi.common.third.weixin.common.constant;

import cn.jzyunqi.common.feature.redis.Cache;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@AllArgsConstructor
public enum WxCache implements Cache {
    WX_MP_V(0L, Boolean.FALSE);

    private final Long expiration;

    private final Boolean autoRenew;
}
