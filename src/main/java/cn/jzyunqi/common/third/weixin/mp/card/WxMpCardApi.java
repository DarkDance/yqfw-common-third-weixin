package cn.jzyunqi.common.third.weixin.mp.card;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.mp.card.enums.CardType;
import cn.jzyunqi.common.third.weixin.mp.card.model.CashCardData;
import cn.jzyunqi.common.third.weixin.mp.card.model.DiscountCardData;
import cn.jzyunqi.common.third.weixin.mp.card.model.GeneralCouponData;
import cn.jzyunqi.common.third.weixin.mp.card.model.GiftCardData;
import cn.jzyunqi.common.third.weixin.mp.card.model.GrouponCardData;
import cn.jzyunqi.common.third.weixin.mp.card.model.MemberCardData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpActivateUserFormParam;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpCardData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpCardParam;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpCardReq;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpLandingPageData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpLandingPageParam;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpMemberCardActiveParam;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpMemberCardUpdateData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpMemberCardUpdateParam;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpMemberCardUserInfoData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpQrcodeData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpQrcodeParam;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApi;
import jakarta.annotation.Resource;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
public class WxMpCardApi extends WxMpTokenApi {

    @Resource
    private WxMpCardApiProxy wxMpCardApiProxy;

    //微信卡券 - 创建卡券
    public String createCard(String wxMpAppId, WxMpCardData cardData) throws BusinessException {
        WxMpCardParam card = new WxMpCardParam();
        if (cardData instanceof MemberCardData memberCardData) {
            card.setCardType(CardType.MEMBER_CARD);
            card.setMemberCard(memberCardData);
        } else if (cardData instanceof GrouponCardData grouponCardData) {
            card.setCardType(CardType.GROUPON);
            card.setGroupon(grouponCardData);
        } else if (cardData instanceof CashCardData cashCardData) {
            card.setCardType(CardType.CASH);
            card.setCash(cashCardData);
        } else if (cardData instanceof DiscountCardData discountCardData) {
            card.setCardType(CardType.DISCOUNT);
            card.setDiscount(discountCardData);
        } else if (cardData instanceof GiftCardData giftCardData) {
            card.setCardType(CardType.GIFT);
            card.setGift(giftCardData);
        } else if (cardData instanceof GeneralCouponData generalCouponData) {
            card.setCardType(CardType.GENERAL_COUPON);
            card.setGeneralCoupon(generalCouponData);
        } else {
            throw new BusinessException("不支持的卡券类型：" + cardData.getClass().getName());
        }
        WxMpCardReq request = new WxMpCardReq();
        request.setCard(card);
        return wxMpCardApiProxy.createCard(getClientToken(wxMpAppId), request).getCardId();
    }

    //微信卡券 - 查询卡券详情（含审核状态）
    public WxMpCardData getCardDetail(String wxMpAppId, WxMpCardData request) throws BusinessException {
        return wxMpCardApiProxy.getCardDetail(getClientToken(wxMpAppId), request);
    }

    //微信卡券 - 投放卡券 - 二维码投放
    public WxMpQrcodeData createQrcodeCard(String wxMpAppId, WxMpQrcodeParam request) throws BusinessException {
        return wxMpCardApiProxy.createQrcodeCard(getClientToken(wxMpAppId), request);
    }

    //微信卡券 - 投放卡券 - 卡券货架投放
    public WxMpLandingPageData createLandingPageCard(String wxMpAppId, WxMpLandingPageParam request) throws BusinessException {
        return wxMpCardApiProxy.createLandingPageCard(getClientToken(wxMpAppId), request);
    }

    //微信卡券 - 激活会员卡(一键激活前置条件设置)
    public void setActivateUserForm(String wxMpAppId, WxMpActivateUserFormParam request) throws BusinessException {
        wxMpCardApiProxy.setActivateUserForm(getClientToken(wxMpAppId), request);
    }

    //微信卡券 - 激活会员卡
    public void activateMemberCard(String wxMpAppId, WxMpMemberCardActiveParam request) throws BusinessException {
        wxMpCardApiProxy.activateMemberCard(getClientToken(wxMpAppId), request);
    }

    //微信卡券 - 拉取会员信息(一键激活的信息获取)
    public WxMpMemberCardUserInfoData getUserInfo(String wxMpAppId, String cardId, String code) throws BusinessException {
        WxMpCardData request = new WxMpCardData();
        request.setCardId(cardId);
        request.setCode(code);
        return wxMpCardApiProxy.getMemberCardUserInfo(getClientToken(wxMpAppId), request);
    }

    //微信卡券 - 更新会员信息
    public WxMpMemberCardUpdateData updateUserMemberCard(String wxMpAppId, WxMpMemberCardUpdateParam request) throws BusinessException {
        return wxMpCardApiProxy.updateUserMemberCard(getClientToken(wxMpAppId), request);
    }
}
