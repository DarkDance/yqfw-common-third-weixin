package cn.jzyunqi.common.third.weixin.open;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import cn.jzyunqi.common.third.weixin.open.user.WxOpenUserApi;
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
    public WxOpenUserApi user;

}
