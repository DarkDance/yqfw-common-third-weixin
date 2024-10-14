package cn.jzyunqi.common.third.weixin.mp.material.model;

import cn.jzyunqi.common.third.weixin.mp.material.enums.MaterialType;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class WxMpMediaData extends WeixinRspV1 {
    /**
     * 类型
     */
    private MaterialType type;

    /**
     * media_id
     */
    private String mediaId;

    /**
     * thumb_media_id
     */
    private String thumbMediaId;

    /**
     * url
     */
    private String url;

    /**
     * 创建时间戳
     */
    private Long createdAt;
}
