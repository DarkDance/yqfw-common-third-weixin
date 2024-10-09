package cn.jzyunqi.common.third.weixin.mp.card.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
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
public class CardData extends WeixinRspV1 {

    @JsonProperty("card_id")
    private String cardId;

    /**
     * 卡券类型
     */
    @JsonProperty("card_type")
    private CardType cardType;

    /**
     * 基本信息.
     */
    @JsonProperty("base_info")
    private BaseInfoData baseInfo;

    /**
     * 创建优惠券特有的高级字段.
     */
    @JsonProperty("advanced_info")
    private AdvancedInfoData advancedInfo;
}
