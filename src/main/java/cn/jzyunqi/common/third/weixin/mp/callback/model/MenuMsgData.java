package cn.jzyunqi.common.third.weixin.mp.callback.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/3/9
 */
@Getter
@Setter
public class MenuMsgData extends BaseMsgData{

    /**
     * 事件KEY值，跳转的小程序路径
     */
    private String eventKey;

    /**
     * 菜单ID，如果是个性化菜单，则可以通过这个字段，知道是哪个规则的菜单被点击了
     */
    private String menuId;

}
