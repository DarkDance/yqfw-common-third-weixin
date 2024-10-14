package cn.jzyunqi.common.third.weixin.open;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
public interface WxOpenClientConfig {

    /**
     * 应用唯一标识
     */
    String getAppId();

    /**
     * 应用密钥
     */
    String getAppSecret();
}
