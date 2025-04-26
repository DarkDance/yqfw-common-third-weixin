package cn.jzyunqi.common.third.weixin.mp;

import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        List<WxMpAuth> wxMpAuthList = getWxMpAuthList();
        for (WxMpAuth wxMpAuth : wxMpAuthList) {
            authMap.put(wxMpAuth.getAppId(), wxMpAuth);
        }
    }

    public WxMpAuth choosWxMpAuth(String wxMpAppId) {
        return authMap.get(wxMpAppId);
    }

    public abstract List<WxMpAuth> getWxMpAuthList();
}
