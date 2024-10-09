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
public class CashCardData extends CardData {

    /**
     * 代金券专用，表示起用金额（单位为分）,如果无起用门槛则填0
     */
    @JsonProperty("least_cost")
    private int leastCost;

    /**
     * 代金券专用，表示减免金额。（单位为分）
     */
    @JsonProperty("reduce_cost")
    private int reduceCost;
}
