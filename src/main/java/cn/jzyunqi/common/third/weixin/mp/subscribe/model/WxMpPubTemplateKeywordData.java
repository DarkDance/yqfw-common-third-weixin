package cn.jzyunqi.common.third.weixin.mp.subscribe.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpPubTemplateKeywordData {
    private int kid;
    private String name;
    private String example;
    private String rule;
}
