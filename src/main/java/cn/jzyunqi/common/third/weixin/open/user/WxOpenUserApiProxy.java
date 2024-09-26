package cn.jzyunqi.common.third.weixin.open.user;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import cn.jzyunqi.common.third.weixin.open.user.model.OpenUserData;
import cn.jzyunqi.common.third.weixin.open.user.model.UserTokenData;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@WxHttpExchange
@HttpExchange(url = "https://api.weixin.qq.com", accept = {"application/json"})
public interface WxOpenUserApiProxy {

    //移动应用 - 微信登录功能
    @GetExchange(url = "/sns/oauth2/access_token?grant_type=authorization_code")
    UserTokenData userToken(@RequestParam String appid, @RequestParam String secret, @RequestParam String code) throws BusinessException;

    //移动应用 - 刷新 access_token 有效期
    @GetExchange(url = "/sns/oauth2/refresh_token?grant_type=refresh_token")
    UserTokenData refreshUserToken(@RequestParam String appid, @RequestParam String refresh_token) throws BusinessException;

    //移动应用 - 检验 access_token 是否有效
    @GetExchange(url = "/sns/auth")
    WeixinRspV1 checkUserToken(@RequestParam String access_token, @RequestParam String openid) throws BusinessException;

    //移动应用 - 获取用户个人信息
    @GetExchange(url = "/sns/userinfo")
    OpenUserData userInfo(@RequestParam String access_token, @RequestParam String openid, @RequestParam(required = false) String lang) throws BusinessException;

}
