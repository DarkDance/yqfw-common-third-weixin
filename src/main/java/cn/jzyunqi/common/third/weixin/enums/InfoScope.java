package cn.jzyunqi.common.third.weixin.enums;

/**
 * @author wiiyaya
 * @date 2018/9/19.
 */
public enum InfoScope {

    /**
     * 获取用户基础数据，关注后会给更多数据，静默授权
     */
    snsapi_base,

    /**
     * 获取用户详细数据，没有关注需要用户点击才可以，关注后静默授权
     */
    snsapi_userinfo,
}
