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
public class GrouponCardData extends CardData {
    /**
     * 团购券专用，团购详情
     */
    @JsonProperty("deal_detail")
    private String dealDetail;
}
