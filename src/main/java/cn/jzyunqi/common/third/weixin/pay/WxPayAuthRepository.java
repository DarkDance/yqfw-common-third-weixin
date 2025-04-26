package cn.jzyunqi.common.third.weixin.pay;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.mp.WxMpAuth;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
public abstract class WxPayAuthRepository implements InitializingBean {

    private final Map<String, WxPayAuth> authMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<WxPayAuth> wxPayAuthList = getWxPayAuthList();
        for (WxPayAuth wxPayAuth : wxPayAuthList) {
            authMap.put(wxPayAuth.getWxAppId(), wxPayAuth);
        }
    }

    public WxPayAuth choosWxPayAuth(String wxAppId) {
        return authMap.get(wxAppId);
    }

    protected abstract List<WxPayAuth> getWxPayAuthList();
}
