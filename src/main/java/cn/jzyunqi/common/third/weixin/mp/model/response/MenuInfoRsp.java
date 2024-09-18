package cn.jzyunqi.common.third.weixin.mp.model.response;

import cn.jzyunqi.common.third.weixin.mp.model.request.MenuParam;
import cn.jzyunqi.common.third.weixin.common.response.WeixinRsp;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

/**
 * @author wiiyaya
 * @since 2018/8/22.
 */
@Getter
@Setter
public class MenuInfoRsp extends WeixinRsp {
    @Serial
    private static final long serialVersionUID = 1047190426231490578L;

    /**
     * 默认目录
     */
    private MenuParam menu;

    /**
     * 个性化目录
     */
    private List<MenuParam> conditional;
}
