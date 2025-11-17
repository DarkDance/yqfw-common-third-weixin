package cn.jzyunqi.common.third.weixin.cp.token;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.model.ThirdTokenRedisDto;
import cn.jzyunqi.common.third.weixin.common.constant.WxCache;
import cn.jzyunqi.common.third.weixin.cp.WxCpAuth;
import cn.jzyunqi.common.third.weixin.cp.WxCpAuthHelper;
import cn.jzyunqi.common.third.weixin.mp.token.model.ClientTokenData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author wiiyaya
 * @since 2025/11/17
 */
@Slf4j
public class WxCpTokenApi {

    @Resource
    private WxCpTokenApiProxy wxCpTokenApiProxy;

    @Resource
    private WxCpAuthHelper wxCpAuthHelper;

    @Resource
    private RedisHelper redisHelper;

    public String getCorporateToken(String corpId) throws BusinessException {
        WxCpAuth wxCpAuth = wxCpAuthHelper.chooseWxCpAuth(corpId);
        return redisHelper.lockAndGet(WxCache.THIRD_WX_CP_V, corpId, Duration.ofSeconds(3), (locked) -> {
            if (locked) {
                ClientTokenData clientTokenData = wxCpTokenApiProxy.getCpToken(wxCpAuth.getCorpId(), wxCpAuth.getCorpSecret());
                ThirdTokenRedisDto clientToken = new ThirdTokenRedisDto();
                clientToken.setToken(clientTokenData.getAccessToken()); //获取到的凭证
                clientToken.setExpireTime(LocalDateTime.now().plusSeconds(clientTokenData.getExpiresIn()).minusSeconds(120)); //凭证有效时间，单位：秒

                redisHelper.vPut(WxCache.THIRD_WX_CP_V, corpId, clientToken);
                return clientTokenData.getAccessToken();
            } else {
                ThirdTokenRedisDto clientToken = (ThirdTokenRedisDto) redisHelper.vGet(WxCache.THIRD_WX_CP_V, corpId);
                if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
                    return clientToken.getToken();
                } else {
                    return null;
                }
            }
        });
    }
}
