package cn.jzyunqi.common.third.weixin.mp.card;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpLandingPageData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpLandingPageParam;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpQrcodeData;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpQrcodeParam;
import cn.jzyunqi.common.third.weixin.mp.card.model.WxMpCardReq;
import cn.jzyunqi.common.third.weixin.mp.card.model.CardData;
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
    CardData createCard(@RequestParam String access_token, @RequestBody WxMpCardReq request) throws BusinessException;

    //微信卡券 - 查询卡券详情（含审核状态）
    @PostExchange(url = "/card/get")
    CardData getCardDetail(@RequestParam String access_token, @RequestBody CardData request) throws BusinessException;

    //微信卡券 - 投放卡券 - 二维码投放
    @PostExchange(url = "/card/qrcode/create")
    WxMpQrcodeData createQrcodeCard(@RequestParam String access_token, @RequestBody WxMpQrcodeParam request) throws BusinessException;

    //微信卡券 - 投放卡券 - 卡券货架投放
    @PostExchange(url = "/card/landingpage/create")
    WxMpLandingPageData createLandingPageCard(@RequestParam String access_token, @RequestBody WxMpLandingPageParam request) throws BusinessException;

    //微信卡券 - 投放卡券 - 群发投放(图文消息群发卡券、分组群发卡券、OpenID列表群发卡券、 客服消息下发卡券)，见消息群发接口
}
