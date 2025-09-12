package cn.jzyunqi.common.third.weixin.mp.user;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApi;
import cn.jzyunqi.common.third.weixin.mp.user.model.MpUserData;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.annotation.Resource;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
public class WxMpUserApi {

    @Resource
    private WxMpTokenApi wxMpTokenApi;

    @Resource
    private WxMpUserApiProxy wxMpUserApiProxy;

    //用户管理 - 获取用户基本信息
    public MpUserData userInfo(String wxMpAppId, String openid) throws BusinessException {
        return userInfo(wxMpAppId, openid, null);
    }

    //用户管理 - 获取用户基本信息
    public MpUserData userInfo(String wxMpAppId, String openid, String lang) throws BusinessException {
        lang = StringUtilPlus.defaultString(lang, "zh_CN");
        return wxMpUserApiProxy.userInfo(wxMpTokenApi.getClientToken(wxMpAppId), openid, lang);
    }
}
