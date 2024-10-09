package cn.jzyunqi.common.third.weixin.mp.card.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
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
public class WxMpMemberCardUpdateData extends WeixinRspV1 {

    /**
     * 用户openid
     */
    private String openId;

    /**
     * 当前用户积分总额
     */
    private Integer resultBonus;

    /**
     * 当前用户预存总金额
     */
    private Double resultBalance;
}
