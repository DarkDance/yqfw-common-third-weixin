package cn.jzyunqi.common.third.weixin.mp;

import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
public abstract class WxMpAuthRepository implements InitializingBean {

    private final Map<String, WxMpAuth> authMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<WxMpAuth> wxMpAuthList = getWxMpAuthList();
        for (WxMpAuth wxMpAuth : wxMpAuthList) {
            authMap.put(wxMpAuth.getAppId(), wxMpAuth);
        }
    }

    public WxMpAuth choosWxMpAuth(String wxMpAppId) {
        return authMap.get(wxMpAppId);
    }

    public void addWxMpAuth(WxMpAuth wxMpAuth) {
        authMap.put(wxMpAuth.getAppId(), wxMpAuth);
    }

    public void removeWxMpAuth(String wxMpAppId) {
        authMap.remove(wxMpAppId);
    }

    public abstract List<WxMpAuth> getWxMpAuthList();
}
