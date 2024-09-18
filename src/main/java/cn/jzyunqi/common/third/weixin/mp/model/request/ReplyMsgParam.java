package cn.jzyunqi.common.third.weixin.mp.model.request;

import cn.jzyunqi.common.third.weixin.mp.model.enums.MsgType;
import cn.jzyunqi.common.third.weixin.mp.model.request.item.ItemData;
import cn.jzyunqi.common.third.weixin.mp.model.request.item.MassFilterData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
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
public class ReplyMsgParam implements Serializable {
    private static final long serialVersionUID = -921746818037924980L;

    /**
     * 群发过滤设置
     */
    @JsonProperty("filter")
    @XmlTransient
    private MassFilterData filter;

    /**
     * 消息创建时间
     */
    @JsonIgnore
    @XmlElement(name = "CreateTime")
    private Integer createTime;

    /**
     * 消息接收方(OpenID)
     */
    @JsonProperty("touser")
    @XmlElement(name = "ToUserName")
    private String toUserName;

    /**
     * 消息发送方(开发者微信号)
     */
    @JsonIgnore
    @XmlElement(name = "FromUserName")
    private String fromUserName;

    /**
     * 消息类型
     */
    @JsonProperty("msgtype")
    @XmlElement(name = "MsgType")
    private MsgType msgType;

    /**
     * 文本消息：消息内容(TODO 能否和text整合起来？)
     */
    @JsonIgnore
    @XmlElement(name = "Content")
    private String content;

    /**
     * 文本消息：消息内容
     */
    @JsonProperty("text")
    @XmlTransient
    private ItemData text;

    /**
     * 图片消息：消息内容
     */
    @JsonProperty("image")
    @XmlElement(name = "Image")
    private ItemData image;

    /**
     * 语音消息：消息内容
     */
    @JsonProperty("voice")
    @XmlElement(name = "Voice")
    private ItemData voice;

    /**
     * 视频消息：消息内容
     */
    @JsonProperty("video")
    @XmlElement(name = "Video")
    private ItemData video;

    /**
     * 音乐消息：消息内容
     */
    @JsonProperty("music")
    @XmlElement(name = "Music")
    private ItemData music;

    /**
     * 图文消息：消息数量
     */
    @JsonIgnore
    @XmlElement(name = "ArticleCount")
    private Integer articleCount;

    /**
     * 图文消息(点击跳转到外链)：消息内容
     */
    @JsonProperty("news")
    @XmlElement(name = "Articles")
    private ItemListParam articles;

    /**
     * 图文消息(点击跳转到图文消息页面)：消息内容
     */
    @JsonProperty("mpnews")
    @XmlTransient
    private ItemData mpArticles;

    /**
     * 发送卡券：消息内容
     */
    @JsonProperty("wxcard")
    @XmlTransient
    private ItemData wxCard;

    /**
     * 客服账号：转移至客服使用
     */
    @JsonProperty("customservice")
    @XmlElement(name = "TransInfo")
    private ItemData csAcc;

    /**
     * 图文消息被判定为转载时，是否继续群发。 1为继续群发（转载），0为停止群发。 该参数默认为0。
     */
    @JsonProperty("send_ignore_reprint")
    @XmlTransient
    private Boolean sendIgnoreReprint;

    /**
     * 自定义消息id，避免重复推送
     */
    @JsonProperty("clientmsgid")
    @XmlTransient
    private String clientMsgId;
}
