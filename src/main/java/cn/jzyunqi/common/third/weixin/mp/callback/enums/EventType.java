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
     * 模板消息事件
     */
    TEMPLATESENDJOBFINISH,
    ;
}
