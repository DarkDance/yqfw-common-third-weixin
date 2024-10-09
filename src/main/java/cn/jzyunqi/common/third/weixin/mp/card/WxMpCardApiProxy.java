package cn.jzyunqi.common.third.weixin.mp.card;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
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

}
