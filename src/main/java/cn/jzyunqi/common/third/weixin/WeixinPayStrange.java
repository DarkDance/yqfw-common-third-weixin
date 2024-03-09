package cn.jzyunqi.common.third.weixin;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.pay.PayHelper;
import cn.jzyunqi.common.third.weixin.client.WeixinPayV3Client;

import java.math.BigDecimal;

/**
 * @author wiiyaya
 * @date 2024/3/9
 */
public class WeixinPayStrange implements PayHelper {

    private final WeixinPayV3Client weixinPayV3Client;

    public WeixinPayStrange(WeixinPayV3Client weixinPayV3Client){
        this.weixinPayV3Client = weixinPayV3Client;
    }

    @Override
    public Object signForPay(String uniqueNo, BigDecimal amount, int expiresInMinutes, boolean creditSupport, String remark, String openId) throws BusinessException {
        return weixinPayV3Client.signForPay(uniqueNo, remark, amount, expiresInMinutes, openId);
    }
}
