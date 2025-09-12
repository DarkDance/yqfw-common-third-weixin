package cn.jzyunqi.common.third.weixin.mp.menu;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRspV1;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuData;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuRsp;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuTryMatchParam;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMpSelfMenuInfoRsp;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApi;
import jakarta.annotation.Resource;

/**
 * @author wiiyaya
 * @since 2025/9/12
 */
public class WxMpMenuApi {

    @Resource
    private WxMpTokenApi wxMpTokenApi;

    @Resource
    private WxMpMenuApiProxy wxMpMenuApiProxy;

    //自定义菜单 - 创建接口
    public WeixinRspV1 menuCreate(String wxMpAppId, WxMenuData request) throws BusinessException {
        if (request.getMatchRule() != null) {
            return wxMpMenuApiProxy.selfMenuCreate(wxMpTokenApi.getClientToken(wxMpAppId), request);
        } else {
            return wxMpMenuApiProxy.menuCreate(wxMpTokenApi.getClientToken(wxMpAppId), request);
        }
    }

    //自定义菜单 - 查询接口（包括官网设置的菜单和AIP设置的菜单）
    public WxMpSelfMenuInfoRsp allMenuInfo(String wxMpAppId) throws BusinessException {
        return wxMpMenuApiProxy.allMenuInfo(wxMpTokenApi.getClientToken(wxMpAppId));
    }

    //自定义菜单 - 删除接口
    public WeixinRspV1 menuDelete(String wxMpAppId) throws BusinessException {
        return wxMpMenuApiProxy.menuDelete(wxMpTokenApi.getClientToken(wxMpAppId));
    }

    //自定义菜单 - 删除个性化菜单
    public WeixinRspV1 selfMenuDelete(String wxMpAppId, String menuId) throws BusinessException {
        WxMenuData request = new WxMenuData();
        request.setMenuId(menuId);
        return wxMpMenuApiProxy.selfMenuDelete(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //自定义菜单 - 测试个性化菜单匹配结果
    public WxMenuData selfMenuTryMatch(String wxMpAppId, String openId) throws BusinessException {
        WxMenuTryMatchParam request = new WxMenuTryMatchParam();
        request.setUserId(openId);
        return wxMpMenuApiProxy.selfMenuTryMatch(wxMpTokenApi.getClientToken(wxMpAppId), request);
    }

    //自定义菜单 - 获取自定义菜单配置(只能查询API定义的菜单)
    public WxMenuRsp selfMenuGet(String wxMpAppId) throws BusinessException {
        return wxMpMenuApiProxy.selfMenuGet(wxMpTokenApi.getClientToken(wxMpAppId));
    }
}
