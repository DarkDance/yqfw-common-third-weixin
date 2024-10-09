package cn.jzyunqi.common.third.weixin.mp.card.enums;

/**
 * @author wiiyaya
 * @since 2024/10/9
 */
public enum ReviewStatus {
    /**
     * 待审核
     */
    CARD_STATUS_NOT_VERIFY,

    /**
     * 审核失败
     */
    CARD_STATUS_VERIFY_FAIL,

    /**
     * 通过审核
     */
    CARD_STATUS_VERIFY_OK,

    /**
     * 卡券被商户删除
     */
    CARD_STATUS_DELETE,

    /**
     * 在公众平台投放过的卡券
     */
    CARD_STATUS_DISPATCH;
}
