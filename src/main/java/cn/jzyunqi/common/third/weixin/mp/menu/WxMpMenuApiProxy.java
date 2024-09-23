package cn.jzyunqi.common.third.weixin.mp.menu;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.WxHttpExchange;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuData;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuTryMatchParam;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMpSelfMenuInfoRsp;
import cn.jzyunqi.common.third.weixin.mp.menu.model.WxMenuRsp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

/**
 * 自定义菜单API
 *
 * @author wiiyaya
 * @since 2024/9/20
 */
@WxHttpExchange
@HttpExchange(url = "https://api.weixin.qq.com", accept = {"application/json"})
public interface WxMpMenuApiProxy {

    //自定义菜单 - 创建接口
    @PostExchange(url = "/cgi-bin/menu/create")
    WeixinRsp menuCreate(@RequestParam String access_token, @RequestBody WxMenuData request) throws BusinessException;

    //自定义菜单 - 查询接口（包括官网设置的菜单和AIP设置的菜单）
    @GetExchange(url = "/cgi-bin/get_current_selfmenu_info")
    WxMpSelfMenuInfoRsp allMenuInfo(@RequestParam String access_token) throws BusinessException;

    //自定义菜单 - 删除接口
    @GetExchange(url = "/cgi-bin/menu/delete")
    WeixinRsp menuDelete(@RequestParam String access_token) throws BusinessException;

    //自定义菜单 - 创建个性化菜单
    @PostExchange(url = "/cgi-bin/menu/addconditional")
    WxMenuData selfMenuCreate(@RequestParam String access_token, @RequestBody WxMenuData request) throws BusinessException;

    //自定义菜单 - 删除个性化菜单
    @GetExchange(url = "/cgi-bin/menu/delconditional")
    WeixinRsp selfMenuDelete(@RequestParam String access_token, @RequestBody WxMenuData request) throws BusinessException;

    //自定义菜单 - 测试个性化菜单匹配结果
    @GetExchange(url = "/cgi-bin/menu/trymatch")
    WxMenuData selfMenuTryMatch(@RequestParam String access_token, @RequestBody WxMenuTryMatchParam request) throws BusinessException;

    //自定义菜单 - 获取自定义菜单配置(只能查询API定义的菜单)
    @GetExchange(url = "/cgi-bin/menu/get")
    WxMenuRsp selfMenuGet(@RequestParam String access_token) throws BusinessException;
}
