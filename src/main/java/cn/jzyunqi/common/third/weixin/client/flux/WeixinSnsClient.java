package cn.jzyunqi.common.third.weixin.client.flux;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.model.enums.WeixinType;
import cn.jzyunqi.common.third.weixin.model.response.UserInfoRsp;
import cn.jzyunqi.common.third.weixin.model.response.UserTokenRsp;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

/**
 * @author wiiyaya
 * @since 2024/3/19
 */
@Slf4j
public class WeixinSnsClient {

    /**
     * 请求代理
     */
    private final WeixinSnsClient.Proxy proxy;

    /**
     * 应用唯一标识
     */
    private final String appId;

    /**
     * 应用密钥
     */
    private final String appSecret;

    /**
     * 应用类型
     */
    private final WeixinType weixinType;

    private final ObjectMapper objectMapper;

    public WeixinSnsClient(String appId, String appSecret, WeixinType weixinType) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.weixinType = weixinType;
        this.proxy = initProxy();
        this.objectMapper = new ObjectMapper();
    }

    public UserTokenRsp getUserAccessToken(String authCode) throws BusinessException {
        UserTokenRsp userTokenRsp;
        try {
            if(weixinType == WeixinType.MINI_APP){
                userTokenRsp = proxy.getWxMpserAccessToken(appId, appSecret, authCode);
            }else {
                userTokenRsp = proxy.getWxUserAccessToken(appId, appSecret, authCode);
            }
        } catch (Exception e) {
            log.error("======WeixinSnsHelper getUserAccessToken other error[{}]:", weixinType, e);
            throw new BusinessException("common_error_wx_get_auth_token_error");
        }
        //微信不管成功还是失败，返回的都是200，需要通过额外的字段来判断是否真的成功
        if (StringUtilPlus.isEmpty(userTokenRsp.getErrorCode())) {
            return userTokenRsp;
        } else {
            log.error("======WeixinSnsHelper getUserAccessToken 200 error[{}][{}][{}]", weixinType, userTokenRsp.getErrorCode(), userTokenRsp.getErrorMsg());
            throw new BusinessException("common_error_wx_get_auth_token_failed");
        }
    }

    /**
     * 获取用户个人信息
     *
     * @param openid          用户openid
     * @param userAccessToken access_token
     * @return 用户信息
     */
    public UserInfoRsp getUserInfo(String openid, String userAccessToken) throws BusinessException {
        if (weixinType != WeixinType.OPEN) {
            log.error("======WeixinSnsHelper getUserInfo weixinType[{}] error:", weixinType);
            throw new BusinessException("common_error_wx_get_user_info_error");
        }
        UserInfoRsp userInfoRsp;
        try {
            userInfoRsp = proxy.getUserInfo(userAccessToken, openid);
        } catch (Exception e) {
            log.error("======WeixinSnsHelper getUserInfo other error:", e);
            throw new BusinessException("common_error_wx_get_user_info_error");
        }
        //微信不管成功还是失败，返回的都是200，需要通过额外的字段来判断是否真的成功
        if (StringUtilPlus.isEmpty(userInfoRsp.getErrorCode())) {
            return userInfoRsp;
        } else {
            log.error("======WeixinSnsHelper use code[{}] getUserInfo 200 error[{}][{}]", userAccessToken, userInfoRsp.getErrorCode(), userInfoRsp.getErrorMsg());
            throw new BusinessException("common_error_wx_get_user_info_failed");
        }
    }

    /**
     * 签名校验
     */
    public <T> T getEncryptedDataInfo(String sessionKey, String rawData, String signature, String iv, String encryptedData, Class<T> classType) throws BusinessException {
        if (weixinType != WeixinType.MINI_APP) {
            log.error("======WeixinSnsHelper getEncryptedDataInfo weixinType[{}] error:", weixinType);
            throw new BusinessException("common_error_wx_get_encrypted_data_info_failed");
        }
        if (StringUtilPlus.isBlank(signature) || signature.equals(DigestUtilPlus.SHA.sign(rawData + sessionKey, DigestUtilPlus.SHAAlgo._1, Boolean.FALSE))) {
            try {
                String decryptedData = DigestUtilPlus.AES.decryptCBCPKCS7Padding(DigestUtilPlus.Base64.decodeBase64(encryptedData), DigestUtilPlus.Base64.decodeBase64(sessionKey), DigestUtilPlus.Base64.decodeBase64(iv));
                return objectMapper.readValue(decryptedData, classType);
            } catch (Exception e) {
                log.error("======WeixinSnsHelper getEncryptedDataInfo error[{}][{}][{}][{}][{}][{}]", sessionKey, rawData, signature, iv, encryptedData, classType, e);
                throw new BusinessException("common_error_wx_get_encrypted_data_info_failed");
            }
        } else {
            throw new BusinessException("common_error_wx_get_encrypted_data_signature_failed");
        }
    }

    @HttpExchange(url = "https://api.weixin.qq.com/sns", accept = {"application/json"})
    public interface Proxy {
        @GetExchange("/oauth2/access_token?grant_type=authorization_code")
        UserTokenRsp getWxUserAccessToken(@RequestParam String appid, @RequestParam String secret, @RequestParam String code);

        @GetExchange("/jscode2session?grant_type=authorization_code")
        UserTokenRsp getWxMpserAccessToken(@RequestParam String appid, @RequestParam String secret, @RequestParam String code);

        @GetExchange("/userinfo")
        UserInfoRsp getUserInfo(@RequestParam("access_token") String accessToken, @RequestParam("openid") String openId);
    }

    private WeixinSnsClient.Proxy initProxy() {
        WebClient webClient = WebClient.builder().build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WeixinSnsClient.Proxy.class);
    }
}
