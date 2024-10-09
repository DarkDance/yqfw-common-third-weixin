package cn.jzyunqi.common.third.weixin.mp.callback.model;

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
     * 卡券ID
     */
    private String cardId;

    /**
     * 卡券Code码
     */
    private String userCardCode;
}
