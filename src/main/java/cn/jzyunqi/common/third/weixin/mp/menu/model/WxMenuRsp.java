package cn.jzyunqi.common.third.weixin.mp.menu.model;

import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.util.List;

/**
 * @author wiiyaya
 * @since 2018/8/22.
 */
@Getter
@Setter
@ToString
public class WxMenuRsp extends WeixinRspV1 {

    /**
     * 默认目录
     */
    private WxMenuData menu;

    /**
     * 个性化目录
     */
    private List<WxMenuData> conditional;
}
