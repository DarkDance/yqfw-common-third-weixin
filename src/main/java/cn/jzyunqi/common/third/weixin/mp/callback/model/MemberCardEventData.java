package cn.jzyunqi.common.third.weixin.mp.callback.model;

import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/10/9
 */
@Getter
@Setter
public class MemberCardEventData extends EventMsgData {

    /**
     * 事件消息-卡券事件：卡id
     */
    private String cardId;

    /**
     * 事件消息-卡券事件：卡code
     */
    private String userCardCode;

    /**
     * 事件消息-卡券事件：是否为转赠领取，1代表是，0代表否。
     */
    private Boolean isGiveByFriend;

    /**
     * 事件消息-卡券事件：当IsGiveByFriend为1时填入的字段，表示发起转赠用户的openid
     */
    private String friendUserName;

    /**
     * 事件消息-卡券事件：？？
     */
    private String outerId;

    /**
     * 事件消息-卡券事件：为保证安全，微信会在转赠发生后变更该卡券的code号，该字段表示转赠前的code。
     */
    private String oldUserCardCode;

    /**
     * 事件消息-卡券事件：用户删除会员卡后可重新找回，当用户本次操作为找回时，该值为1，否则为0
     */
    private Boolean isRestoreMemberCard;

    /**
     * 事件消息-卡券事件：？？
     */
    private Boolean isRecommendByFriend;

    /**
     * 事件消息-卡券事件： ？？
     */
    private String sourceScene;

    /**
     * 事件消息-卡券事件：领券用户的UnionId
     */
    private String unionId;

}
