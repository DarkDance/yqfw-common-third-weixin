package cn.jzyunqi.common.third.weixin.mp.material.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpMaterialCountData extends WeixinRspV1 {
    @JsonProperty("voice_count")
    private Integer voiceCount;

    @JsonProperty("video_count")
    private Integer videoCount;

    @JsonProperty("image_count")
    private Integer imageCount;

    @JsonProperty("news_count")
    private Integer newsCount;
}
