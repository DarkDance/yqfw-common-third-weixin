package cn.jzyunqi.common.third.weixin.mp.token;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.model.ThirdTokenRedisDto;
import cn.jzyunqi.common.third.weixin.common.constant.WxCache;
import cn.jzyunqi.common.third.weixin.mp.WxMpAuth;
import cn.jzyunqi.common.third.weixin.mp.WxMpAuthHelper;
import cn.jzyunqi.common.third.weixin.mp.token.enums.TicketType;
import cn.jzyunqi.common.third.weixin.mp.token.model.ClientTokenData;
import cn.jzyunqi.common.third.weixin.mp.token.model.TicketRedisDto;
import cn.jzyunqi.common.third.weixin.mp.token.model.TicketRsp;
import cn.jzyunqi.common.third.weixin.mp.token.model.WxJsapiSignature;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
@Slf4j
public class WxMpTokenApi {

    @Resource
    private WxMpTokenApiProxy wxMpTokenApiProxy;

    @Resource
    private WxMpAuthHelper wxMpAuthHelper;

    @Resource
    private RedisHelper redisHelper;

    public WxJsapiSignature createJsapiSignature(String wxMpAppId, String url) throws BusinessException {
        WxMpAuth wxMpAuth = wxMpAuthHelper.chooseWxMpAuth(wxMpAppId);
        long timestamp = System.currentTimeMillis() / 1000;//从1970年1月1日00:00:00至今的秒数
        String nonceStr = RandomUtilPlus.String.nextAlphanumeric(32);
        String jsapiTicket = getTicket(wxMpAuth.getAppId(), TicketType.JSAPI);
        //注意这里参数名必须全部小写，且必须有序
        String needSign = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", jsapiTicket, nonceStr, timestamp, url);
        String signature = DigestUtilPlus.SHA.sign(needSign, DigestUtilPlus.SHAAlgo._1, Boolean.FALSE);

        log.debug("needSign:[{}]", needSign);
        WxJsapiSignature jsapiSignature = new WxJsapiSignature();
        jsapiSignature.setAppId(wxMpAuth.getAppId());
        jsapiSignature.setTimestamp(timestamp);
        jsapiSignature.setNonceStr(nonceStr);
        jsapiSignature.setUrl(url);//这个url不能删除，删除后jssdk会失败，文档上没有这个字段
        jsapiSignature.setSignature(signature);
        return jsapiSignature;
    }

    protected String getClientToken(String wxMpAppId) throws BusinessException {
        WxMpAuth wxMpAuth = wxMpAuthHelper.chooseWxMpAuth(wxMpAppId);
        String tokenKey = getClientTokenKey(wxMpAppId);
        return redisHelper.lockAndGet(WxCache.THIRD_WX_MP_V, tokenKey, Duration.ofSeconds(3), (locked) -> {
            if (locked) {
                ClientTokenData clientTokenData = wxMpTokenApiProxy.getClientToken(wxMpAuth.getAppId(), wxMpAuth.getAppSecret());
                ThirdTokenRedisDto clientToken = new ThirdTokenRedisDto();
                clientToken.setToken(clientTokenData.getAccessToken()); //获取到的凭证
                clientToken.setExpireTime(LocalDateTime.now().plusSeconds(clientTokenData.getExpiresIn()).minusSeconds(120)); //凭证有效时间，单位：秒

                redisHelper.vPut(WxCache.THIRD_WX_MP_V, tokenKey, clientToken);
                return clientTokenData.getAccessToken();
            } else {
                ThirdTokenRedisDto clientToken = (ThirdTokenRedisDto) redisHelper.vGet(WxCache.THIRD_WX_MP_V, tokenKey);
                if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
                    return clientToken.getToken();
                } else {
                    return null;
                }
            }
        });
    }

    private String getTicket(String wxMpAppId, TicketType type) throws BusinessException {
        String ticketKey = chooseTicketKey(wxMpAppId, type);
        return redisHelper.lockAndGet(WxCache.THIRD_WX_MP_V, ticketKey, Duration.ofSeconds(3), (locked) -> {
            if (locked) {
                TicketRsp ticketRsp = wxMpTokenApiProxy.getTicket(getClientToken(wxMpAppId), type.getCode());
                TicketRedisDto ticketRedisDto = new TicketRedisDto();
                ticketRedisDto.setTicket(ticketRsp.getTicket()); //获取到的凭证
                ticketRedisDto.setExpireTime(LocalDateTime.now().plusSeconds(ticketRsp.getExpiresIn()).minusSeconds(120)); //凭证有效时间，单位：秒

                redisHelper.vPut(WxCache.THIRD_WX_MP_V, ticketKey, ticketRedisDto);
                return ticketRsp.getTicket();
            } else {
                TicketRedisDto ticketRedisDto = (TicketRedisDto) redisHelper.vGet(WxCache.THIRD_WX_MP_V, ticketKey);
                if (ticketRedisDto != null && LocalDateTime.now().isBefore(ticketRedisDto.getExpireTime())) {
                    return ticketRedisDto.getTicket();
                } else {
                    return null;
                }
            }
        });
    }

    private String getClientTokenKey(String wxMpAppId) {
        return "client_token:" + wxMpAppId;
    }

    private String chooseTicketKey(String wxMpAppId, TicketType type) {
        return switch (type) {
            case JSAPI -> "jsapi_ticket:" + wxMpAppId;
            case WX_CARD -> "wx_card_ticket:" + wxMpAppId;
            case SDK -> "sdk_ticket:" + wxMpAppId;
        };
    }
}
