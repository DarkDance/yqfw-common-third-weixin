package cn.jzyunqi.common.third.weixin.open.user;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import cn.jzyunqi.common.third.weixin.mp.WxMpAuth;
import cn.jzyunqi.common.third.weixin.open.WxOpenAuth;
import cn.jzyunqi.common.third.weixin.open.WxOpenAuthHelper;
import cn.jzyunqi.common.third.weixin.open.user.model.OpenUserData;
import cn.jzyunqi.common.third.weixin.open.user.model.UserTokenData;
import jakarta.annotation.Resource;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
public class WxOpenUserApi {

    @Resource
    private WxOpenUserApiProxy wxOpenUserApiProxy;

    @Resource
    private WxOpenAuthHelper wxOpenAuthHelper;

    //移动应用 - 微信登录功能
    public UserTokenData userToken(String wxOpenAppId, String code) throws BusinessException {
        WxOpenAuth wxOpenAuth = wxOpenAuthHelper.chooseWxOpenAuth(wxOpenAppId);
        return wxOpenUserApiProxy.userToken(wxOpenAuth.getAppId(), wxOpenAuth.getAppSecret(), code);
    }

    //移动应用 - 刷新 access_token 有效期
    public UserTokenData refreshUserToken(String wxOpenAppId, String userRrefreshToken) throws BusinessException {
        WxOpenAuth wxOpenAuth = wxOpenAuthHelper.chooseWxOpenAuth(wxOpenAppId);
        return wxOpenUserApiProxy.refreshUserToken(wxOpenAuth.getAppId(), userRrefreshToken);
    }

    //移动应用 - 检验 access_token 是否有效
    public WeixinRspV1 checkUserToken(String userAccessToken, String openid) throws BusinessException {
        return wxOpenUserApiProxy.checkUserToken(userAccessToken, openid);
    }

    //移动应用 - 获取用户个人信息
    public OpenUserData userInfo(String userAccessToken, String openid) throws BusinessException {
        return wxOpenUserApiProxy.userInfo(userAccessToken, openid, "zh_CN");
    }
}
