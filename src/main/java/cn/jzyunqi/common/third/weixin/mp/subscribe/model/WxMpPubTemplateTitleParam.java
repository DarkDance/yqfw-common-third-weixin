package cn.jzyunqi.common.third.weixin.mp.subscribe.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpPubTemplateTitleParam {
    /**
     * 类目 id，多个用逗号隔开
     */
    private String ids;

    /**
     * 用于分页，表示从 start 开始，从 0 开始计数
     */
    private Integer start;

    /**
     * 用于分页，表示拉取 limit 条记录，最大为 30
     */
    private Integer limit;
}
