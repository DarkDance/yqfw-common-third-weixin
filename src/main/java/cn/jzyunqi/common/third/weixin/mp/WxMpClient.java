package cn.jzyunqi.common.third.weixin.mp;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.LockType;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.weixin.common.constant.WxCache;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgDetailCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.MsgSimpleCb;
import cn.jzyunqi.common.third.weixin.mp.callback.model.ReplyMsgData;
import cn.jzyunqi.common.third.weixin.mp.kefu.WxMpKfApiProxy;
import cn.jzyunqi.common.third.weixin.mp.kefu.enums.TypingType;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfAccountParam;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfMsgListParam;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfMsgListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfSessionData;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfSessionListRsp;
import cn.jzyunqi.common.third.weixin.mp.kefu.model.WxMpKfTypingParam;
import cn.jzyunqi.common.third.weixin.mp.material.WxMpMaterialApiProxy;
import cn.jzyunqi.common.third.weixin.mp.material.enums.MaterialType;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialCountData;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialNewsData;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialSearchParam;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialSearchRsp;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMaterialVideoRsp;
import cn.jzyunqi.common.third.weixin.mp.material.model.WxMpMediaData;
import cn.jzyunqi.common.third.weixin.mp.menu.WxMpMenuApiProxy;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuData;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuRsp;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuTryMatchParam;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMpSelfMenuInfoRsp;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApiProxy;
import cn.jzyunqi.common.third.weixin.mp.token.enums.TicketType;
import cn.jzyunqi.common.third.weixin.mp.token.model.ClientTokenData;
import cn.jzyunqi.common.third.weixin.mp.token.model.ClientTokenRedisDto;
import cn.jzyunqi.common.third.weixin.mp.token.model.TicketRedisDto;
import cn.jzyunqi.common.third.weixin.mp.token.model.TicketRsp;
import cn.jzyunqi.common.third.weixin.mp.token.model.WxJsapiSignature;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 公众号客户端
 *
 * @author wiiyaya
 * @since 2024/9/23
 */
@Slf4j
public class WxMpClient {

    private static final String WX_JS_API_TICKET_SIGN = "jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s";

    @Resource
    private WxMpTokenApiProxy wxMpTokenApiProxy;

    @Resource
    private WxMpKfApiProxy wxMpKefuApiProxy;

    @Resource
    private WxMpMenuApiProxy wxMpMenuApiProxy;

    @Resource
    private WxMpMaterialApiProxy wxMpMaterialApiProxy;

    @Resource
    @Getter
    private WxMpConfig wxMpConfig;

    @Resource
    private RedisHelper redisHelper;

    public final Kf kf = new Kf();
    public final Menu menu = new Menu();
    public final Material material = new Material();

    public class Kf {
        //客服管理 - 添加客服账号（添加后不可用，需要再邀请）
        public WeixinRsp kfAccountAdd(WxMpKfAccountParam request) throws BusinessException {
            return wxMpKefuApiProxy.kfAccountAdd(getClientToken(), request);
        }

        //客服管理 - 邀请绑定客服账号
        public WeixinRsp kfAccountInviteWorker(WxMpKfAccountParam request) throws BusinessException {
            return wxMpKefuApiProxy.kfAccountInviteWorker(getClientToken(), request);
        }

        //客服管理 - 修改客服账号
        public WeixinRsp kfAccountUpdate(WxMpKfAccountParam request) throws BusinessException {
            return wxMpKefuApiProxy.kfAccountUpdate(getClientToken(), request);
        }

        //客服管理 - 删除客服账号
        public WeixinRsp kfAccountDel(String kfAccount) throws BusinessException {
            WxMpKfAccountParam request = new WxMpKfAccountParam();
            request.setKfAccount(kfAccount);
            return wxMpKefuApiProxy.kfAccountDel(getClientToken(), request);
        }

        //客服管理 - 设置客服账号的头像，文件大小为5M以内
        public WeixinRsp kfAccountUploadHeadImg(String kfAccount, MultipartFile media) throws BusinessException {
            return wxMpKefuApiProxy.kfAccountUploadHeadImg(getClientToken(), kfAccount, media);
        }

        //客服管理 - 获取所有客服账号
        public WxMpKfListRsp kfList() throws BusinessException {
            return wxMpKefuApiProxy.kfList(getClientToken());
        }

        //客服管理 - 获取所有客服在线账号
        public WxMpKfListRsp kfOnlineList() throws BusinessException {
            return wxMpKefuApiProxy.kfOnlineList(getClientToken());
        }

        //客服消息 - 发消息
        public String sendKefuMessageWithResponse(ReplyMsgData request) throws BusinessException {
            return wxMpKefuApiProxy.sendKefuMessageWithResponse(getClientToken(), request);
        }

        //客服消息 - 发送客服输入状态
        public String sendKfTypingState(String openid, TypingType command) throws BusinessException {
            WxMpKfTypingParam request = new WxMpKfTypingParam();
            request.setToUser(openid);
            request.setCommand(command);
            return wxMpKefuApiProxy.sendKfTypingState(getClientToken(), request);
        }

        //会话控制 - 创建会话
        public String kfSessionCreate(String openid, String kfAccount) throws BusinessException {
            WxMpKfSessionData request = new WxMpKfSessionData();
            request.setOpenid(openid);
            request.setKfAccount(kfAccount);
            return wxMpKefuApiProxy.kfSessionCreate(getClientToken(), request);
        }

        //会话控制 - 关闭会话
        public String kfSessionClose(String openid, String kfAccount) throws BusinessException {
            WxMpKfSessionData request = new WxMpKfSessionData();
            request.setOpenid(openid);
            request.setKfAccount(kfAccount);
            return wxMpKefuApiProxy.kfSessionClose(getClientToken(), request);
        }

        //会话控制 - 获取客户会话状态
        public WxMpKfSessionData kfSessionInfo(String openid) throws BusinessException {
            return wxMpKefuApiProxy.kfSessionInfo(getClientToken(), openid);
        }

        //会话控制 - 获取客服会话列表
        public WxMpKfSessionListRsp kfSessionList(String kfAccount) throws BusinessException {
            return wxMpKefuApiProxy.kfSessionList(getClientToken(), kfAccount);
        }

        //会话控制 - 获取未接入会话列表
        public WxMpKfSessionListRsp kfSessionWaitCase() throws BusinessException {
            return wxMpKefuApiProxy.kfSessionWaitCase(getClientToken());
        }

        //获取聊天记录
        public WxMpKfMsgListRsp kfMsgList(LocalDateTime startTime, LocalDateTime endTime, Long msgId, Integer number) throws BusinessException {
            WxMpKfMsgListParam request = new WxMpKfMsgListParam();
            request.setStartTime(DateTimeUtilPlus.toEpochSecond(startTime));
            request.setEndTime(DateTimeUtilPlus.toEpochSecond(endTime));
            request.setMsgId(msgId);
            request.setNumber(number);
            return wxMpKefuApiProxy.kfMsgList(getClientToken(), request);
        }
    }

    public class Menu {
        //自定义菜单 - 创建接口
        public WeixinRsp menuCreate(WxMenuData request) throws BusinessException {
            if (request.getMatchRule() != null) {
                return wxMpMenuApiProxy.selfMenuCreate(getClientToken(), request);
            } else {
                return wxMpMenuApiProxy.menuCreate(getClientToken(), request);
            }
        }

        //自定义菜单 - 查询接口（包括官网设置的菜单和AIP设置的菜单）
        public WxMpSelfMenuInfoRsp allMenuInfo() throws BusinessException {
            return wxMpMenuApiProxy.allMenuInfo(getClientToken());
        }

        //自定义菜单 - 删除接口
        public WeixinRsp menuDelete() throws BusinessException {
            return wxMpMenuApiProxy.menuDelete(getClientToken());
        }

        //自定义菜单 - 删除个性化菜单
        public WeixinRsp selfMenuDelete(String menuId) throws BusinessException {
            WxMenuData request = new WxMenuData();
            request.setMenuId(menuId);
            return wxMpMenuApiProxy.selfMenuDelete(getClientToken(), request);
        }

        //自定义菜单 - 测试个性化菜单匹配结果
        public WxMenuData selfMenuTryMatch(String openId) throws BusinessException {
            WxMenuTryMatchParam request = new WxMenuTryMatchParam();
            request.setUserId(openId);
            return wxMpMenuApiProxy.selfMenuTryMatch(getClientToken(), request);
        }

        //自定义菜单 - 获取自定义菜单配置(只能查询API定义的菜单)
        public WxMenuRsp selfMenuGet() throws BusinessException {
            return wxMpMenuApiProxy.selfMenuGet(getClientToken());
        }
    }

    public class Material {
        //素材管理 - 新增临时素材
        public WxMpMediaData mediaUpload(MaterialType type, MultipartFile media) throws BusinessException {
            return wxMpMaterialApiProxy.mediaUpload(getClientToken(), type, media);
        }

        //素材管理 - 获取临时素材
        public File mediaDownload(String mediaId) throws BusinessException {
            return wxMpMaterialApiProxy.mediaDownload(getClientToken(), mediaId);
        }

        //素材管理 - 获取高清语音素材
        public File jssdkMediaDownload(String mediaId) throws BusinessException {
            return wxMpMaterialApiProxy.jssdkMediaDownload(getClientToken(), mediaId);
        }

        //素材管理 - 新增图片永久素材
        public WxMpMediaData mediaImgUpload(MultipartFile media) throws BusinessException {
            return wxMpMaterialApiProxy.mediaImgUpload(getClientToken(), media);
        }

        //素材管理 - 新增其它永久素材
        public WxMpMediaData materialOtherUpload(MaterialType type, MultipartFile media) throws BusinessException {
            return wxMpMaterialApiProxy.materialOtherUpload(getClientToken(), type, media);
        }

        //素材管理 - 图文永久素材获取
        public WxMpMaterialNewsData materialNewsInfo(String mediaId) throws BusinessException {
            WxMpMediaData request = new WxMpMediaData();
            request.setMediaId(mediaId);
            return wxMpMaterialApiProxy.materialNewsInfo(getClientToken(), request);
        }

        //素材管理 - 视频永久素材获取
        public WxMpMaterialVideoRsp materialVideoInfo(String mediaId) throws BusinessException {
            WxMpMediaData request = new WxMpMediaData();
            request.setMediaId(mediaId);
            return wxMpMaterialApiProxy.materialVideoInfo(getClientToken(), request);
        }

        //素材管理 - 其它永久素材获取
        public InputStream materialOtherDownload(String mediaId) throws BusinessException {
            WxMpMediaData request = new WxMpMediaData();
            request.setMediaId(mediaId);
            return wxMpMaterialApiProxy.materialOtherDownload(getClientToken(), request);
        }

        //素材管理 - 删除永久素材
        public WeixinRsp materialDelete(String mediaId) throws BusinessException {
            WxMpMediaData request = new WxMpMediaData();
            request.setMediaId(mediaId);
            return wxMpMaterialApiProxy.materialDelete(getClientToken(), request);
        }

        //素材管理 - 获取永久素材总数
        public WxMpMaterialCountData materialCount() throws BusinessException {
            return wxMpMaterialApiProxy.materialCount(getClientToken());
        }

        //素材管理 - 获取永久素材列表
        public WxMpMaterialSearchRsp materialBatchGet(MaterialType type, Integer offset, Integer count) throws BusinessException {
            WxMpMaterialSearchParam request = new WxMpMaterialSearchParam();
            request.setType(type);
            request.setOffset(offset);
            request.setCount(count);
            return wxMpMaterialApiProxy.materialBatchGet(getClientToken(), request);
        }
    }

    public WxJsapiSignature createJsapiSignature(String url) throws BusinessException {
        long timestamp = System.currentTimeMillis() / 1000;//从1970年1月1日00:00:00至今的秒数
        String nonceStr = RandomUtilPlus.String.randomAlphanumeric(32);
        String jsapiTicket = getTicket(TicketType.JSAPI);
        //注意这里参数名必须全部小写，且必须有序
        String needSign = String.format(WX_JS_API_TICKET_SIGN, jsapiTicket, nonceStr, timestamp, url);
        String signature = DigestUtilPlus.SHA.sign(needSign, DigestUtilPlus.SHAAlgo._1, Boolean.FALSE);

        WxJsapiSignature jsapiSignature = new WxJsapiSignature();
        jsapiSignature.setAppId(wxMpConfig.getAppId());
        jsapiSignature.setTimestamp(timestamp);
        jsapiSignature.setNonceStr(nonceStr);
        jsapiSignature.setUrl(url);
        jsapiSignature.setSignature(signature);
        return jsapiSignature;
    }

    private String getClientToken() throws BusinessException {
        ClientTokenRedisDto clientToken = (ClientTokenRedisDto) redisHelper.vGet(WxCache.WX_MP, wxMpConfig.getClientTokenKey());
        if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
            return clientToken.getToken();
        }
        Lock lock = redisHelper.getLock(WxCache.WX_MP, wxMpConfig.getClientTokenKey().concat(":lock"), LockType.NORMAL);
        long timeOutMillis = System.currentTimeMillis() + 3000;
        boolean locked = false;
        try {
            do {
                // 防止多线程同时获取accessToken
                clientToken = (ClientTokenRedisDto) redisHelper.vGet(WxCache.WX_MP, wxMpConfig.getClientTokenKey());
                if (clientToken != null && LocalDateTime.now().isBefore(clientToken.getExpireTime())) {
                    return clientToken.getToken();
                }

                locked = lock.tryLock(100, TimeUnit.MILLISECONDS);
                if (!locked && System.currentTimeMillis() > timeOutMillis) {
                    throw new InterruptedException("获取accessToken超时：获取时间超时");
                }
            } while (!locked);

            //获取到锁的服务可以去获取accessToken
            ClientTokenData clientTokenData = wxMpTokenApiProxy.getClientToken(wxMpConfig.getAppId(), wxMpConfig.getAppSecret());
            clientToken = new ClientTokenRedisDto();
            clientToken.setToken(clientTokenData.getAccessToken()); //获取到的凭证
            clientToken.setExpireTime(LocalDateTime.now().plusSeconds(clientTokenData.getExpiresIn()).minusSeconds(120)); //凭证有效时间，单位：秒

            redisHelper.vPut(WxCache.WX_MP, wxMpConfig.getClientTokenKey(), clientToken);
            return clientTokenData.getAccessToken();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    private String getTicket(TicketType type) throws BusinessException {
        String ticketKey = chooseTicketKey(type);
        TicketRedisDto ticketRedisDto = (TicketRedisDto) redisHelper.vGet(WxCache.WX_MP, ticketKey);
        if (ticketRedisDto != null && LocalDateTime.now().isBefore(ticketRedisDto.getExpireTime())) {
            return ticketRedisDto.getTicket();
        }
        Lock lock = redisHelper.getLock(WxCache.WX_MP, ticketKey.concat(":lock"), LockType.NORMAL);
        long timeOutMillis = System.currentTimeMillis() + 3000;
        boolean locked = false;
        try {
            do {
                // 防止多线程同时获取accessToken
                ticketRedisDto = (TicketRedisDto) redisHelper.vGet(WxCache.WX_MP, ticketKey);
                if (ticketRedisDto != null && LocalDateTime.now().isBefore(ticketRedisDto.getExpireTime())) {
                    return ticketRedisDto.getTicket();
                }

                locked = lock.tryLock(100, TimeUnit.MILLISECONDS);
                if (!locked && System.currentTimeMillis() > timeOutMillis) {
                    throw new InterruptedException("获取ticket超时：获取时间超时:" + type);
                }
            } while (!locked);

            //获取到锁的服务可以去获取accessToken
            TicketRsp ticketRsp = wxMpTokenApiProxy.getTicket(getClientToken(), type.getCode());
            ticketRedisDto = new TicketRedisDto();
            ticketRedisDto.setTicket(ticketRsp.getTicket()); //获取到的凭证
            ticketRedisDto.setExpireTime(LocalDateTime.now().plusSeconds(ticketRsp.getExpiresIn()).minusSeconds(120)); //凭证有效时间，单位：秒

            redisHelper.vPut(WxCache.WX_MP, ticketKey, ticketRedisDto);
            return ticketRsp.getTicket();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    private String chooseTicketKey(TicketType type) {
        return switch (type) {
            case JSAPI -> wxMpConfig.getJsapiTicketKey();
            case WX_CARD -> wxMpConfig.getWxCardTicketKey();
            case SDK -> wxMpConfig.getSdkTicketKey();
        };
    }
}
