package cn.jzyunqi.common.third.weixin.mp.material.model;

import cn.jzyunqi.common.third.weixin.mp.material.enums.MaterialType;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;

/**
 * @author wiiyaya
 * @since 2018/8/24.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class WxMpMediaData extends WeixinRsp {
    @Serial
    private static final long serialVersionUID = 7921679008435737891L;

    /**
     * 类型
     */
    private MaterialType type;

    /**
     * media_id
     */
    @JsonProperty("media_id")
    private String mediaId;

    /**
     * thumb_media_id
     */
    @JsonProperty("thumb_media_id")
    private String thumbMediaId;

    /**
     * url
     */
    private String url;

    /**
     * 创建时间戳
     */
    @JsonProperty("created_at")
    private Long createdAt;
}
