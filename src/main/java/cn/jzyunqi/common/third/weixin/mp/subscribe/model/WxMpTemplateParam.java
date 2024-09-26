package cn.jzyunqi.common.third.weixin.mp.subscribe.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpTemplateParam {
    /**
     * 模板标题 id，可通过getPubTemplateTitleList接口获取，也可登录公众号后台查看获取
     */
    private String tid;

    /**
     * 开发者自行组合好的模板关键词列表，关键词顺序可以自由搭配（例如 [3,5,4] 或 [4,5,3]），最多支持5个，最少2个关键词组合
     */
    private List<Integer> kidList;

    /**
     * 服务场景描述，15个字以内
     */
    private String sceneDesc;
}
