package cn.jzyunqi.common.third.weixin.open;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import cn.jzyunqi.common.third.weixin.open.user.WxOpenUserApiProxy;
import cn.jzyunqi.common.third.weixin.open.user.model.OpenUserData;
import cn.jzyunqi.common.third.weixin.open.user.model.UserTokenData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 开放平台客户端
 *
 * @author wiiyaya
 * @since 2024/9/25
 */
@Slf4j
public class WxOpenClient {

    @Resource
    private WxOpenUserApiProxy wxOpenUserApiProxy;

    @Resource
    private WxOpenClientConfig wxOpenClientConfig;

    public final User user = new User();

    public class User {
        //移动应用 - 微信登录功能
        public UserTokenData userToken(String code) throws BusinessException {
            return wxOpenUserApiProxy.userToken(wxOpenClientConfig.getAppId(), wxOpenClientConfig.getAppSecret(), code);
        }

        //移动应用 - 刷新 access_token 有效期
        public UserTokenData refreshUserToken(String userRrefreshToken) throws BusinessException {
            return wxOpenUserApiProxy.refreshUserToken(wxOpenClientConfig.getAppId(), userRrefreshToken);
        }

        //移动应用 - 检验 access_token 是否有效
        public WeixinRsp checkUserToken(String userAccessToken, String openid) throws BusinessException {
            return wxOpenUserApiProxy.checkUserToken(userAccessToken, openid);
        }

        //移动应用 - 获取用户个人信息
        public OpenUserData userInfo(String userAccessToken, String openid) throws BusinessException {
            return wxOpenUserApiProxy.userInfo(userAccessToken, openid, "zh_CN");
        }
    }
}
