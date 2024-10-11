package cn.jzyunqi.common.third.weixin.mp.callback.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/3/9
 */
@Getter
@Setter
public class VoiceMsgData extends BaseMsgData {
    /**
     * 图片消息/语音消息/视频消息：消息媒体id，可以调用多媒体文件下载接口拉取数据
     */
    private String mediaId;

    /**
     * 语音消息：语音格式，如amr，speex等
     */
    private String format;

    /**
     * 语音消息：语音识别结果，UTF8编码
     * 由于客户端缓存，开发者开启或者关闭语音识别功能，对新关注者立刻生效，对已关注用户需要24小时生效。开发者可以重新关注此帐号进行测试
     */
    private String recognition;

    /**
     * 16K采样率语音消息媒体id
     */
    private String mediaId16K;
}
