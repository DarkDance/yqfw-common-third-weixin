package cn.jzyunqi.common.third.weixin.mp.user;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.mp.user.model.MpUserData;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * @author wiiyaya
 * @since 2024/9/24
 */
@WxHttpExchange
@HttpExchange(url = "https://api.weixin.qq.com", accept = {"application/json"})
public interface WxMpUserApiProxy {

    //用户管理 - 获取用户基本信息
    @GetExchange(url = "/cgi-bin/user/info")
    MpUserData userInfo(@RequestParam String access_token, @RequestParam String openid, @RequestParam String lang) throws BusinessException;
}
