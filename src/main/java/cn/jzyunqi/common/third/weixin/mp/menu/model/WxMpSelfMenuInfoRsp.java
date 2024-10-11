package cn.jzyunqi.common.third.weixin.mp.menu.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpSelfMenuInfoRsp extends WeixinRspV1 {

    @JsonProperty("selfmenu_info")
    private WxMpSelfMenuData selfMenuInfo;

    private Boolean isMenuOpen;
}
