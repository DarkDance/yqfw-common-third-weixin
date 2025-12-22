package cn.jzyunqi.common.third.weixin.pay.cert;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.weixin.common.constant.WxCache;
import cn.jzyunqi.common.third.weixin.pay.WxPayAuth;
import cn.jzyunqi.common.third.weixin.pay.cert.model.PlantCertData;
import cn.jzyunqi.common.third.weixin.pay.cert.model.PlantCertRedisDto;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
@Slf4j
public class WxPayCertApi {

    @Resource
    private WxPayCertApiProxy wxPayCertApiProxy;

    @Resource
    private RedisHelper redisHelper;

    public PlantCertRedisDto plantCert(WxPayAuth wxPayAuth, String weixinPemSerial) throws BusinessException {
        //如果没有证书编号且已经下载过证书了，忽略这个请求
        if (StringUtilPlus.isBlank(weixinPemSerial)) {
            Map<String, Object> redisWeixinPem = redisHelper.hGetAll(WxCache.THIRD_WX_PAY_H, wxPayAuth.getMerchantId());
            if (CollectionUtilPlus.Map.isNotEmpty(redisWeixinPem)) {
                return null;
            }
        }

        String redisKey = wxPayAuth.getMerchantId();
        return redisHelper.lockAndGet(WxCache.THIRD_WX_PAY_H, weixinPemSerial, Duration.ofSeconds(5), (locked) -> {
            if (locked) {
                List<PlantCertData> plantCertDataList = wxPayCertApiProxy.certDownload(wxPayAuth.getWxAppId()).getData();
                Map<String, Object> weixinPem = new HashMap<>();
                PlantCertRedisDto needReturn = null;
                try {
                    for (PlantCertData certData : plantCertDataList) {
                        String cipherText = certData.getEncryptCertificate().getCipherText();
                        String nonce = certData.getEncryptCertificate().getNonce();
                        String associatedData = certData.getEncryptCertificate().getAssociatedData();
                        String pem = DigestUtilPlus.AES.decryptGCM(DigestUtilPlus.Base64.decodeBase64(cipherText), wxPayAuth.getMerchantAesKey().getBytes(StringUtilPlus.UTF_8), nonce.getBytes(StringUtilPlus.UTF_8), associatedData.getBytes(StringUtilPlus.UTF_8));

                        //通过服务器证书获取服务器支付公钥
                        CertificateFactory cf = CertificateFactory.getInstance("X509");
                        X509Certificate x509Cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(pem.getBytes(StringUtilPlus.UTF_8)));
                        x509Cert.checkValidity();
                        String weixinPublicKey = DigestUtilPlus.Base64.encodeBase64String(x509Cert.getPublicKey().getEncoded());

                        //存入缓存
                        PlantCertRedisDto plantCertRedisDto = new PlantCertRedisDto();
                        plantCertRedisDto.setSerialNo(certData.getSerialNo());
                        plantCertRedisDto.setEffectiveTime(certData.getEffectiveTime().toLocalDateTime());
                        plantCertRedisDto.setExpireTime(certData.getExpireTime().toLocalDateTime());
                        plantCertRedisDto.setPublicKey(weixinPublicKey);
                        weixinPem.put(certData.getSerialNo(), plantCertRedisDto);

                        if (StringUtilPlus.CS.equals(plantCertRedisDto.getSerialNo(), weixinPemSerial)) {
                            needReturn = plantCertRedisDto;
                        }
                    }
                    redisHelper.hPutAll(WxCache.THIRD_WX_PAY_H, redisKey, weixinPem);
                    return needReturn;
                } catch (Exception e) {
                    log.error("weixin plantCert error: ", e);
                    return null;
                }
            } else {
                if (StringUtilPlus.isBlank(weixinPemSerial)) {
                    return null;
                }
                PlantCertRedisDto plantCertRedisDto = (PlantCertRedisDto) redisHelper.hGet(WxCache.THIRD_WX_PAY_H, redisKey, weixinPemSerial);
                if (plantCertRedisDto != null && LocalDateTime.now().isBefore(plantCertRedisDto.getExpireTime())) {
                    return plantCertRedisDto;
                } else {
                    return null;
                }
            }
        });
    }
}
