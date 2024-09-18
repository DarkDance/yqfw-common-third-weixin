package cn.jzyunqi.common.third.weixin.mp.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.mp.model.enums.WeixinType;
import cn.jzyunqi.common.third.weixin.mp.model.response.UserBaseInfoRsp;
import cn.jzyunqi.common.third.weixin.mp.model.response.UserTokenRsp;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

/**
 * 大部分为开放平台用户相关接口
 *
 * @author wiiyaya
 * @since 2018/5/22.
 */
@Slf4j
public class WeixinSnsClient {

    /**
     * 通过code获取access_token的接口
     */
    private static final String WX_USER_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 通过code获取access_token的接口
     */
    private static final String WX_MP_USER_TOKEN_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    /**
     * 通过个人授权获取用户个人信息
     */
    private static final String WX_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

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

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public WeixinSnsClient(String appId, String appSecret, WeixinType weixinType) {
        this.appId = appId;
        this.appSecret = appSecret;
        this.weixinType = weixinType;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 通过code获取access_token
     *
     * @param authCode 用户换取access_token的code
     * @return UserTokenRsp
     */
    public UserTokenRsp getUserAccessToken(String authCode) throws BusinessException {
        UserTokenRsp userTokenRsp;
        try {
            URI accessTokenUri = switch (weixinType) {
                case OPEN, MP -> new URIBuilder(String.format(WX_USER_TOKEN_URL, appId, appSecret, authCode)).build();
                case MINI_APP -> new URIBuilder(String.format(WX_MP_USER_TOKEN_URL, appId, appSecret, authCode)).build();
            };

            RequestEntity<Void> requestEntity = new RequestEntity<>(HttpMethod.GET, accessTokenUri);
            ResponseEntity<UserTokenRsp> weixinUserRsp = restTemplate.exchange(requestEntity, UserTokenRsp.class);
            userTokenRsp = Optional.ofNullable(weixinUserRsp.getBody()).orElseGet(UserTokenRsp::new);
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
    public UserBaseInfoRsp getUserInfo(String openid, String userAccessToken) throws BusinessException {
        if (weixinType != WeixinType.OPEN) {
            log.error("======WeixinSnsHelper getUserInfo weixinType[{}] error:", weixinType);
            throw new BusinessException("common_error_wx_get_user_info_error");
        }
        UserBaseInfoRsp userInfoRsp;
        try {
            URI weixinUserInfoUri = new URIBuilder(String.format(WX_USER_INFO_URL, userAccessToken, openid)).build();

            RequestEntity<Map<String, String>> requestEntity = new RequestEntity<>(HttpMethod.GET, weixinUserInfoUri);
            ResponseEntity<UserBaseInfoRsp> weixinUserRsp = restTemplate.exchange(requestEntity, UserBaseInfoRsp.class);
            userInfoRsp = Optional.ofNullable(weixinUserRsp.getBody()).orElseGet(UserBaseInfoRsp::new);
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
}
