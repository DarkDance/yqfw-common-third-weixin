package cn.jzyunqi.common.third.weixin.mp.model.callback.item;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/3/9
 */
@Getter
@Setter
public class ImageMsgData extends BaseMsgData{

    /**
     * 图片消息：图片链接（由微信生成）
     */
    private String picUrl;

    /**
     * 图片消息/语音消息/视频消息：消息媒体id，可以调用多媒体文件下载接口拉取数据
     */
    private Long mediaId;
}
