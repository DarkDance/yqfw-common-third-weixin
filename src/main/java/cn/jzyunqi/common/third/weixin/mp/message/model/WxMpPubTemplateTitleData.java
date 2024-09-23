package cn.jzyunqi.common.third.weixin.mp.message.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpPubTemplateTitleData {
    private Integer type;

    private Integer tid;

    private String categoryId;

    private String title;
}
