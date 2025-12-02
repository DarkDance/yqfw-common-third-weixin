package cn.jzyunqi.common.third.weixin.cp.callback.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2025/12/2
 */
@Getter
@Setter
@ToString
public class TextChatData extends BaseChatData{

    private Text text;

    @Getter
    @Setter
    @ToString
    public static class Text {

        /**
         * 消息内容
         */
        private String content;
    }
}
