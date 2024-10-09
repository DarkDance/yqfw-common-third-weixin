package cn.jzyunqi.common.third.weixin.mp.card.model;

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
public class DiscountCardData extends CardData {
    /**
     * 折扣券专用，表示打折额度（百分比）。填30就是七折。
     */
    private int discount;
}
