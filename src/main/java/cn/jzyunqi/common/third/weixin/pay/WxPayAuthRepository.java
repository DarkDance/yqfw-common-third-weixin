package cn.jzyunqi.common.third.weixin.pay;

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

    public void addWxPayAuth(WxPayAuth wxPayAuth) {
        authMap.put(wxPayAuth.getWxAppId(), wxPayAuth);
    }

    public void removeWxPayAuth(String wxAppId) {
        authMap.remove(wxAppId);
    }

    public abstract List<WxPayAuth> getWxPayAuthList();
}
