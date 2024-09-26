package cn.jzyunqi.common.third.weixin.mp.subscribe.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpMsgTemplateData extends WeixinRspV1 {

    /**
     * 添加至账号下的模板id，发送订阅通知时所需
     */
    private String priTmplId;

    /**
     * 模版标题
     */
    private String title;

    /**
     * 模版内容
     */
    private String content;

    /**
     * 模板内容示例
     */
    private String example;

    /**
     * 模版类型，2 为一次性订阅，3 为长期订阅
     */
    private Integer type;
}
