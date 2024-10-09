package cn.jzyunqi.common.third.weixin.mp.card.model;

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
public class GeneralCouponData extends WxMpCardData {
    /**
     * 兑换券专用，填写兑换内容的名称.
     */
    @JsonProperty("default_detail")
    private String defaultDetail;
}
