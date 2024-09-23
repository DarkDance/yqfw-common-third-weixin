package cn.jzyunqi.common.third.weixin.mp.callback.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/3/9
 */
@Getter
@Setter
public class TextMsgData extends BaseMsgData {

    /**
     * 文本消息内容
     */
    private String content;
}
