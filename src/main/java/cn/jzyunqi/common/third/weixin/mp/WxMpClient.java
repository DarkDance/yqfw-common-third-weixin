package cn.jzyunqi.common.third.weixin.mp;

import cn.jzyunqi.common.third.weixin.mp.card.WxMpCardApi;
import cn.jzyunqi.common.third.weixin.mp.kefu.WxMpKefuApi;
import cn.jzyunqi.common.third.weixin.mp.mass.WxMpMassApi;
import cn.jzyunqi.common.third.weixin.mp.material.WxMpMaterialApi;
import cn.jzyunqi.common.third.weixin.mp.menu.WxMpMenuApi;
import cn.jzyunqi.common.third.weixin.mp.subscribe.WxMpSubscribeMsgApi;
import cn.jzyunqi.common.third.weixin.mp.template.WxMpTemplateMsgApi;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApi;
import cn.jzyunqi.common.third.weixin.mp.user.WxMpUserApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * 公众号客户端
 *
 * @author wiiyaya
 * @since 2024/9/23
 */
@Slf4j
public class WxMpClient {

    @Resource
    public WxMpTokenApi token;

    @Resource
    public WxMpKefuApi kf;

    @Resource
    public WxMpMenuApi menu;

    @Resource
    public WxMpMaterialApi material;

    @Resource
    public WxMpUserApi user;

    @Resource
    public WxMpMassApi mass;

    @Resource
    public WxMpSubscribeMsgApi subscribe;

    @Resource
    public WxMpTemplateMsgApi template;

    @Resource
    public WxMpCardApi card;
}
