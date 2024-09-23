package cn.jzyunqi.common.third.weixin.mp.material.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2018/8/19.
 */

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class WxMpItemData implements Serializable {
    @Serial
    private static final long serialVersionUID = 8926590897226459420L;
    /**
     * 图文消息标题
     */
    @JsonProperty("title")
    @XmlElement(name = "Title")
    private String title;

    /**
     * 文本内容
     */
    @JsonProperty("content")
    @XmlTransient
    private String content;

    /**
     * 图文消息描述
     */
    @JsonProperty("description")
    @XmlElement(name = "Description")
    private String description;

    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     */
    @JsonProperty("picurl")
    @XmlElement(name = "PicUrl")
    private String picUrl;

    /**
     * 点击图文消息跳转链接
     */
    @JsonProperty("url")
    @XmlElement(name = "Url")
    private String url;

    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id
     */
    @JsonProperty("media_id")
    @XmlElement(name = "MediaId")
    private String mediaId;

    /**
     * 音乐链接
     */
    @JsonProperty("musicurl")
    @XmlElement(name = "MusicUrl")
    private String musicUrl;

    /**
     * 高质量音乐链接，WIFI环境优先使用该链接播放音乐
     */
    @JsonProperty("hqmusicurl")
    @XmlElement(name = "HQMusicUrl")
    private String hqMusicUrl;

    /**
     * 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id, 小程序卡片图片建议大小为520*416
     */
    @JsonProperty("thumb_media_id")
    @XmlElement(name = "ThumbMediaId")
    private String thumbMediaId;

    /**
     * 卡券code
     */
    @JsonProperty("card_id")
    @XmlTransient
    private String cardId;

    /**
     * 客服账户
     */
    @JsonProperty("kf_account")
    @XmlElement(name = "KfAccount")
    private String kfAccount;

    /**
     * 小程序的appid
     */
    @JsonProperty("appid")
    @XmlTransient
    private String appId;

    /**
     * 小程序的appid
     */
    @JsonProperty("pagepath")
    @XmlTransient
    private String pagePath;


    /**
     * 图文消息的作者(原创文章推送用)
     */
    @JsonProperty("author")
    @XmlTransient
    private String author;


    /**
     * 在图文消息页面点击“阅读原文”后的页面(原创文章推送用)
     */
    @JsonProperty("content_source_url")
    @XmlTransient
    private String contentSourceUrl;


    /**
     * 图文消息的描述(原创文章推送用)
     */
    @JsonProperty("digest")
    @XmlTransient
    private String digest;

    /**
     * 是否显示封面，1为显示，0为不显示(原创文章推送用)
     */
    @JsonProperty("show_cover_pic")
    @XmlTransient
    private Integer showCoverPic;

    /**
     * 是否打开评论，0不打开，1打开(原创文章推送用)
     */
    @JsonProperty("need_open_comment")
    @XmlTransient
    private Integer needOpenComment;

    /**
     * 是否粉丝才可评论，0所有人可评论，1粉丝才可评论(原创文章推送用)
     */
    @JsonProperty("only_fans_can_comment")
    @XmlTransient
    private Integer onlyFansCanComment;
}