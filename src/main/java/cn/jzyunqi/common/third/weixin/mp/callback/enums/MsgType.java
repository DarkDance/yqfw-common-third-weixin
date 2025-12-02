package cn.jzyunqi.common.third.weixin.mp.callback.enums;

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

    /**
     * 撤回消息
     */
    revoke,

    /**
     * 同意消息
     */
    agree,

    /**
     * 不同意消息
     */
    disagree,

    /**
     * 卡片消息
     */
    card,

    /**
     * 表情消息
     */
    emotion,

    /**
     * 文件消息
     */
    file,

    /**
     * 小程序消息
     */
    weapp,

    /**
     * 会话记录消息
     */
    chatrecord,

    /**
     * 待办消息
     */
    todo,

    /**
     * 投票消息
     */
    vote,

    /**
     * 填表消息
     */
    collect,

    /**
     * 红包消息
     */
    redpacket,

    /**
     * 会议邀请消息
     */
    meeting,

    /**
     * 会议控制消息
     */
    meeting_notification,

    /**
     * 在线文档消息
     */
    docmsg,

    /**
     * MarkDown格式消息
     */
    markdown,

    /**
     * 日程消息
     */
    calendar,

    /**
     * 混合消息
     */
    mixed,

    /**
     * 音频存档消息
     */
    meeting_voice_call,

    /**
     * 音频共享文档消息
     */
    voip_doc_share,

    /**
     * 互通红包消息
     */
    external_redpacket,

    /**
     * 视频号消息
     */
    sphfeed,

    /**
     * 音视频通话
     */
    voiptext,

    /**
     * 微盘文件
     */
    qydiskfile,

    ;
}
