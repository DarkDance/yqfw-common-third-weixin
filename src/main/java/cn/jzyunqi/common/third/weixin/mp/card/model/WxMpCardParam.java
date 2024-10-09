package cn.jzyunqi.common.third.weixin.mp.card.model;

import cn.jzyunqi.common.third.weixin.mp.card.enums.CardType;
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
public class WxMpCardParam {

    @JsonProperty("card_type")
    private CardType cardType;

    @JsonProperty("member_card")
    private MemberCardData memberCard;

    private GrouponCardData groupon;

    private CashCardData cash;

    private DiscountCardData discount;

    private GiftCardData gift;

    @JsonProperty("general_coupon")
    private GeneralCouponData generalCoupon;

}
