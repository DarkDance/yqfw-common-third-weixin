package cn.jzyunqi.common.third.weixin.mp.card.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2024/10/9
 */
@Getter
@Setter
@ToString
public class WxMpLandingPageData extends WeixinRspV1 {

    /**
     * 货架链接。
     */
    private String url;

    /**
     * 货架ID。货架的唯一标识。
     */
    @JsonProperty("page_id")
    private String pageId;

}
