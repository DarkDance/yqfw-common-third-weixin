package cn.jzyunqi.common.third.weixin.model.callback.item;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/3/9
 */
@Getter
@Setter
public class VoiceMsgData extends BaseMsgData{
    /**
     * 图片消息/语音消息/视频消息：消息媒体id，可以调用多媒体文件下载接口拉取数据
     */
    private Long mediaId;

    /**
     * 语音消息：语音格式，如amr，speex等
     */
    private String format;

    /**
     * 语音消息：语音识别结果，UTF8编码
     */
    private String recognition;
}
