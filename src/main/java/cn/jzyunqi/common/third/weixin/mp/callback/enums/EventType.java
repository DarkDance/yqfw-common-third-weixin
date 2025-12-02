package cn.jzyunqi.common.third.weixin.mp.callback.enums;

import jakarta.xml.bind.annotation.XmlEnum;

/**
 * @author wiiyaya
 * @since 2018/8/18.
 */
@XmlEnum
public enum EventType {
    /**
     * 关注公众号事件
     */
    subscribe,

    /**
     * 取消关注公众号事件
     */
    unsubscribe,

    /**
     * 上报地理位置事件
     */
    LOCATION,

    /**
     * 点击菜单拉取消息时
     */
    CLICK,

    /**
     * 点击菜单跳转链接时
     */
    VIEW,

    /**
     * 扫码推事件
     */
    scancode_push,

    /**
     * 扫码推事件且弹出“消息接收中”提示框
     */
    scancode_waitmsg,

    /**
     * 弹出系统拍照发图
     */
    pic_sysphoto,

    /**
     * 弹出拍照或者相册发图
     */
    pic_photo_or_album,

    /**
     * 弹出微信相册发图器
     */
    pic_weixin,

    /**
     * 弹出地理位置选择器
     */
    location_select,

    /**
     * 点击菜单跳转小程序的事件推送
     */
    view_miniprogram,

    /**
     * 模板消息事件
     */
    TEMPLATESENDJOBFINISH,

    /**
     * 生成的卡券通过审核时，微信会把这个事件推送到开发者
     */
    card_pass_check,
    card_not_pass_check,

    /**
     * 用户在转赠卡券时，微信会把这个事件推送到开发者
     */
    user_gifting_card,

    /**
     * 用户在删除卡券时，微信会把这个事件推送到开发者
     */
    user_del_card,

    /**
     * 微信买单完成时，微信会把这个事件推送到开发者
     */
    user_pay_from_pay_cell,

    /**
     * 用户在卡券里点击查看公众号进入会话时（需要用户已经关注公众号），微信会把这个事件推送到开发者
     */
    user_enter_session_from_card,

    /**
     * 当用户的会员卡积分余额发生变动时，微信会推送事件告知开发者
     */
    update_member_card,

    /**
     * 当某个card_id的初始库存数大于200且当前库存小于等于100时，用户尝试领券会触发发送事件给商户，事件每隔12h发送一次。
     */
    card_sku_remind,

    /**
     * 当商户朋友的券券点发生变动时，微信服务器会推送消息给商户服务器。
     */
    card_pay_order,

    /**
     * 当用户通过一键激活的方式提交信息并点击激活或者用户修改会员卡信息后，商户会收到用户激活的事件推送
     */
    submit_membercard_user_info,

    /**
     * 用户在领取卡券时，微信会把这个事件推送到开发者
     */
    user_get_card,

    /**
     * 卡券被核销时，微信会把这个事件推送到开发者
     */
    user_consume_card,

    /**
     * 用户在进入会员卡时，微信会把这个事件推送到开发者,需要开发者在首页时填入need_push_on_view 字段并设置为true
     */
    user_view_card,

    /**
     * 企微：会话内容存档变更
     */
    msgaudit_notify
    ;
}
