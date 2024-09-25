package cn.jzyunqi.common.third.weixin.open;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@Getter
@AllArgsConstructor
public class WxOpenClientConfig {

    /**
     * 应用唯一标识
     */
    private final String appId;

    /**
     * 应用密钥
     */
    private final String appSecret;
}
