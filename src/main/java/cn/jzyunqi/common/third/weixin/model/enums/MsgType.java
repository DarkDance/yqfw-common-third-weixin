package cn.jzyunqi.common.third.weixin.model.enums;

import jakarta.xml.bind.annotation.XmlEnum;

/**
 * @author wiiyaya
 * @since 2018/8/18.
 */
@XmlEnum
public enum MsgType {
    /**
     * 文本消息
     */
    text,

    /**
     * 图片消息
     */
    image,

    /**
     * 语音消息
     */
    voice,

    /**
     * 视频消息
     */
    video,

    /**
     * 缩略图
     */
    thumb,

    /**
     * 小视频消息
     */
    shortvideo,

    /**
     * 音乐消息
     */
    music,

    /**
     * 图文消息(点击跳转到外链)
     */
    news,

    /**
     * 图文消息(点击跳转到图文消息页面)
     */
    mpnews,

    /**
     * 发送卡券
     */
    wxcard,

    /**
     * 地理位置消息
     */
    location,

    /**
     * 链接消息
     */
    link,

    /**
     * 事件消息
     */
    event,

    /**
     * 转移到客服事件，内部使用
     */
    transfer_customer_service,

    /**
     * 发送小程序卡片（要求小程序与公众号已关联）
     */
    miniprogrampage,
    ;
}
