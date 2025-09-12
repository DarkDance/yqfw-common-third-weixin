package cn.jzyunqi.common.third.weixin.open;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WxOpenAuth {

    /**
     * 公众号唯一凭证
     */
    private String appId;

    /**
     * 公众号唯一凭证密钥
     */
    private String appSecret;
}
