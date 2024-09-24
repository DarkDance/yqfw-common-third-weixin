package cn.jzyunqi.common.third.weixin.mp.token.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxJsapiSignature {
    private String appId;

    private String nonceStr;

    private long timestamp;

    private String url;

    private String signature;

}
