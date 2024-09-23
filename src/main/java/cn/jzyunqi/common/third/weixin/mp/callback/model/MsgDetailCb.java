package cn.jzyunqi.common.third.weixin.mp.callback.model;

import cn.jzyunqi.common.third.weixin.mp.callback.enums.EventType;
import cn.jzyunqi.common.third.weixin.mp.callback.enums.MsgType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2018/8/18.
 */
@Getter
@Setter
@ToString
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)//字段绑定到XML，默认PROPERTY为get/set方法绑定
public class MsgDetailCb implements Serializable {
    @Serial
    private static final long serialVersionUID = 3741348562928513685L;

    /**
     * 消息接收方
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;

    /**
     * 消息发送方
     */
    @XmlElement(name = "FromUserName")
    private String fromUserName;

    /**
     * 消息创建时间
     */
    @XmlElement(name = "CreateTime")
    private Integer createTime;

    /**
     * 消息类型
     */
    @XmlElement(name = "MsgType")
    private MsgType msgType;

    /**
     * 消息id
     */
    @XmlElement(name = "MsgId")
    private Long msgId;

    /**
     * 消息内容加密字段
     */
    @XmlElement(name = "Encrypt")
    private String encrypt;

    /**
     * 文本消息：文本消息内容
     */
    @XmlElement(name = "Content")
    private String content;

    /**
     * 图片消息：图片链接（由微信生成）
     */
    @XmlElement(name = "PicUrl")
    private String picUrl;

    /**
     * 图片消息/语音消息/视频消息：消息媒体id，可以调用多媒体文件下载接口拉取数据
     */
    @XmlElement(name = "MediaId")
    private Long mediaId;

    /**
     * 语音消息：语音格式，如amr，speex等
     */
    @XmlElement(name = "Format")
    private String format;

    /**
     * 语音消息：语音识别结果，UTF8编码
     */
    @XmlElement(name = "Recognition")
    private String recognition;

    /**
     * 视频消息：视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
     */
    @XmlElement(name = "ThumbMediaId")
    private Long thumbMediaId;

    /**
     * 地理位置消息：地理位置维度
     */
    @XmlElement(name = "Location_X")
    private Double locationX;

    /**
     * 地理位置消息：地理位置经度
     */
    @XmlElement(name = "Location_Y")
    private Double locationY;

    /**
     * 地理位置消息：地图缩放大小
     */
    @XmlElement(name = "Scale")
    private Integer scale;

    /**
     * 地理位置消息：地理位置信息
     */
    @XmlElement(name = "Label")
    private String label;

    /**
     * 链接消息：消息标题
     */
    @XmlElement(name = "Title")
    private String title;

    /**
     * 链接消息：消息描述
     */
    @XmlElement(name = "Description")
    private String description;

    /**
     * 链接消息：消息链接
     */
    @XmlElement(name = "Url")
    private String url;

    /**
     * 事件消息：事件类型
     */
    @XmlElement(name = "Event")
    private EventType event;

    /**
     * 事件消息
     * 1. 扫描带参数二维码事件：事件KEY值，未关注（qrscene_为前缀，后面为二维码的参数值）已关注（创建二维码时的二维码scene_id）
     * 2. 自定义菜单事件-点击菜单拉取消息时的事件推送， 事件KEY值，与自定义菜单接口中KEY值对应
     * 2. 自定义菜单事件-点击菜单跳转链接时的事件推送， 事件KEY值，设置的跳转URL
     */
    @XmlElement(name = "EventKey")
    private String eventKey;

    /**
     * 事件消息-扫描带参数二维码事件：二维码的ticket，可用来换取二维码图片
     */
    @XmlElement(name = "Ticket")
    private String ticket;

    /**
     * 事件消息-上报地理位置事件：地理位置纬度
     */
    @XmlElement(name = "Latitude")
    private Double latitude;

    /**
     * 事件消息-上报地理位置事件：地理位置经度
     */
    @XmlElement(name = "Longitude")
    private Double longitude;

    /**
     * 事件消息-上报地理位置事件：地理位置精度
     */
    @XmlElement(name = "Precision")
    private Double precision;

    //private Long menuId;  //点击菜单跳转链接时的事件推送

    //private ScanCodeInfo ScanCodeInfo; //扫码推事件

    //private SendPicsInfo SendPicsInfo; //弹出系统拍照发图

    //private SendLocationInfo SendLocationInfo;//弹出地理位置选择
}
