package cn.jzyunqi.common.third.weixin.mp.card;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpActivateUserFormParam;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpCardData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpCardReq;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpLandingPageData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpLandingPageParam;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpMemberCardActiveParam;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpMemberCardUpdateData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpMemberCardUpdateParam;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpMemberCardUserInfoData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpQrcodeData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpQrcodeParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * 会员卡专区API
 *
 * @author wiiyaya
 * @since 2024/10/09
 */
@WxHttpExchange
@HttpExchange(url = "https://api.weixin.qq.com", accept = {"application/json"})
public interface WxMpCardApiProxy {

    //微信卡券 - 创建卡券
    @PostExchange(url = "/card/create")
    WxMpCardData createCard(@RequestParam String access_token, @RequestBody WxMpCardReq request) throws BusinessException;

    //微信卡券 - 查询卡券详情（含审核状态）
    @PostExchange(url = "/card/get")
    WxMpCardData getCardDetail(@RequestParam String access_token, @RequestBody WxMpCardData request) throws BusinessException;

    //微信卡券 - 投放卡券 - 二维码投放
    @PostExchange(url = "/card/qrcode/create")
    WxMpQrcodeData createQrcodeCard(@RequestParam String access_token, @RequestBody WxMpQrcodeParam request) throws BusinessException;

    //微信卡券 - 投放卡券 - 卡券货架投放
    @PostExchange(url = "/card/landingpage/create")
    WxMpLandingPageData createLandingPageCard(@RequestParam String access_token, @RequestBody WxMpLandingPageParam request) throws BusinessException;

    //微信卡券 - 投放卡券 - 群发投放(图文消息群发卡券、分组群发卡券、OpenID列表群发卡券、 客服消息下发卡券)，见消息群发接口

    //微信卡券 - 激活会员卡(一键激活前置条件设置)
    @PostExchange(url = "/card/membercard/activateuserform/set")
    WeixinRspV1 setActivateUserForm(@RequestParam String access_token, @RequestBody WxMpActivateUserFormParam request) throws BusinessException;

    //微信卡券 - 激活会员卡
    @PostExchange(url = "/card/membercard/activate")
    WeixinRspV1 activateMemberCard(@RequestParam String access_token, @RequestBody WxMpMemberCardActiveParam request) throws BusinessException;

    //微信卡券 - 拉取会员信息(一键激活的信息获取)
    @PostExchange(url = "/card/membercard/userinfo/get")
    WxMpMemberCardUserInfoData getMemberCardUserInfo(@RequestParam String access_token, @RequestBody WxMpCardData request) throws BusinessException;

    //微信卡券 - 更新会员信息
    @PostExchange(url = "/card/membercard/updateuser")
    WxMpMemberCardUpdateData updateUserMemberCard(@RequestParam String access_token, @RequestBody WxMpMemberCardUpdateParam request) throws BusinessException;

}
