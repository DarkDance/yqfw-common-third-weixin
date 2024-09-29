package cn.jzyunqi.common.third.weixin.mp.callback.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/3/9
 */
@Getter
@Setter
public class VideoMsgData extends BaseMsgData{

    /**
     * 图片消息/语音消息/视频消息：消息媒体id，可以调用多媒体文件下载接口拉取数据
     */
    private String mediaId;

    /**
     * 视频消息：视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
     */
    private String thumbMediaId;

}
