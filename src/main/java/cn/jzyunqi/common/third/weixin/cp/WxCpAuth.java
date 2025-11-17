package cn.jzyunqi.common.third.weixin.cp;

import cn.jzyunqi.common.third.weixin.mp.WxMpAuth;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author wiiyaya
 * @since 2025/11/17
 */
@AllArgsConstructor
@NoArgsConstructor
public class WxCpAuth extends WxMpAuth {

    public String getCorpId() {
        return super.getAppId();
    }

    public void setCorpId(String corpId) {
        super.setAppId(corpId);
    }

    public String getCorpSecret() {
        return super.getAppSecret();
    }

    public void setCorpSecret(String corpSecret) {
        super.setAppSecret(corpSecret);
    }
}
