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
public class GiftCardData extends WxMpCardData {
    /**
     * 兑换券专用，填写兑换内容的名称。
     */
    private String gift;
}
