# 一、项目介绍
yqfw-common-third-weixin是云祺框架的微信生态服务接入模块，提供了微信公众号（WxMp）、微信小程序（WxMa）、微信企业号（WxCp）、微信支付等服务的统一封装。该模块支持微信消息推送与回调处理、用户OAuth授权、微信支付（V2/V3）、小程序码生成、企微消息发送等功能，帮助开发者快速构建微信生态应用。

# 二、项目结构
## 1. 通用结构
第三方模块通用结构如下，每个子模块遵循统一的包及类命名规范：
```
yqfw-common-third-{第三方名}
└── src/main/java
    └── cn.jzyunqi.common.third.{第三方名} # 第三方主包名
        ├── common #模块通用包
        │   ├── constant #模块通用常量包
        │   ├── enums #模块通用枚举包
        │   ├── model #模块通用模型包
        │   ├── utils #模块通用工具包
        │   ├── {第三方名}HttpExchange.java #模块通用切面注解类
        │   └── {第三方名}HttpExchangeWrapper.java #模块通用切面业务类
        └── {子模块名} #子模块
            │── {子模块业务名} #具体业务包，可以没有这一层
            │   ├── enums #业务枚举包
            │   ├── model #业务模型包
            │   ├── utils #业务工具包
            │   ├── A{第三方名}{子模块名}MsgCbController.java #第三方消息回调接口，抽象类，定义消息回调处理方法
            │   ├── {第三方名}{子模块名}{子模块业务名}Api.java #第三方API接口类，封装第三方API接口参数、返回结果及异常处理
            │   └── {第三方名}{子模块名}{子模块业务名}ApiProxy.java #第三方API接口代理类，负责调用第三方Api接口
            │── {第三方名}{子模块名}Auth.java #第三方账户信息类（AppID、AppSecret等）
            │── {第三方名}{子模块名}AuthHelper.java #第三方账户供应助手接口（由业务侧实现，提供认证信息）
            │── {第三方名}{子模块名}Client.java #第三方客户端类（对外暴露服务调用入口）
            └── {第三方名}{子模块名}Config.java #第三方客户端配置类（注册Bean到Spring容器）
```

## 2. 本项目结构
```
yqfw-common-third-weixin
└── src/main/java
    └── cn.jzyunqi.common.third.weixin
        ├── common #模块通用包
        │   ├── constant #模块通用常量包
        │   ├── enums #模块通用枚举包
        │   ├── model #模块通用模型包
        │   └── utils #模块通用工具包
        │── cp #企微子模块
        │   │── callback #企微消息回调包
        │   │   ├── enums #枚举包
        │   │   ├── model #模型类
        │   │   ├── utils #工具类
        │   │── token #企微token包
        │   │   ├── enums #枚举包
        │   │   ├── model #模型类
        │   │   ├── utils #工具类
        │   │── WxCpAuth.java #企微账户信息
        │   │── WxCpAuthHelper.java #企微账户供应助手接口
        │   │── WxCpClient.java #企微客户端
        │   └── WxCpConfig.java #企微客户端配置类
        │── miniapp #小程序子模块
        │   │── qrcode #小程序二维码包
        │   │   ├── enums #枚举包
        │   │   ├── model #模型类
        │   │   ├── utils #工具类
        │   │── WxMaAuth.java #小程序账户信息
        │   │── WxMaAuthHelper.java #小程序账户供应助手接口
        │   │── WxMaClient.java #小程序客户端
        │   └── WxMaConfig.java #小程序客户端配置类
        │── mp #公众号子模块
        │   │── callback #公众号消息回调包
        │   │   ├── enums #枚举包
        │   │   └── model #模型类
        │   │── card #卡券包
        │   │   ├── enums #枚举包
        │   │   └── model #模型类
        │   │── kefu #客服包
        │   │   ├── enums #枚举包
        │   │   └── model #模型类
        │   │── mass #群发包
        │   │   └── model #模型类
        │   │── material #素材包
        │   │   ├── enums #枚举包
        │   │   └── model #模型类
        │   │── menu #菜单包
        │   │   ├── enums #枚举包
        │   │   └── model #模型类
        │   │── subscribe #订阅通知包
        │   │   └── model #模型类
        │   │── template #模板消息包
        │   │   ├── enums #枚举包
        │   │   └── model #模型类
        │   │── token #token包
        │   │   ├── enums #枚举包
        │   │   └── model #模型类
        │   │── user #用户包
        │   │   └── model #模型类
        │   │── WxMpAuth.java #公众号账户信息
        │   │── WxMpAuthHelper.java #公众号账户供应助手接口
        │   │── WxMpClient.java #公众号客户端
        │   └── WxMpConfig.java #公众号客户端配置类
        │── open #开放平台子模块
        │   │── user #用户包
        │   │   └── model #模型类
        │   │── WxOpenAuth.java #开放平台账户信息
        │   │── WxOpenAuthHelper.java #开放平台账户供应助手接口
        │   │── WxOpenClient.java #开放平台客户端
        │   └── WxOpenConfig.java #开放平台客户端配置类
        └── pay #微信支付子模块
            │── callback #支付回调包
            │   └── model #模型类
            │── cert #证书包
            │   └── model #模型类
            │── order #订单包
            │   ├── enums #枚举包
            │   └── model #模型类
            │── tool #工具包
            │── WxPayAuth.java #微信支付账户信息
            │── WxPayAuthHelper.java #微信支付账户供应助手接口
            │── WxPayClient.java #微信支付客户端
            └── WxPayConfig.java #微信支付客户端配置类
```

# 三、使用说明

## 1. 安装依赖
运行mvn clean install命令安装当前包，然后在个人项目中引入如下依赖：
```xml
<dependency>
    <groupId>cn.jzyunqi</groupId>
    <artifactId>yqfw-common-third-weixin</artifactId>
    <version>${yqfw.version}</version>
</dependency>
```

## 2. 账号配置方法
以公众号为例，在个人项目中引入公众号配置如下：
```java
@Import({WxMpConfig.class})
```

配置微信公众号认证信息：
```java
@Bean
public WxMpAuthHelper wxMpAuthHelper() {
    return (wxMpAppId) -> ...; //根据wxMpAppId查询认证信息并返回WxMpAuth对象
}
```

## 3. 使用方法
### * 接入微信消息回调接口方法
1. 新增一个controller并继承AWeixinMsgCbController，在类(非方法)上加上注解`@RequestMapping("/....")`作为微信公众号的回调地址
```java
@Controller
@RequestMapping("/weixin/callback")
public class WeixinMsgCbController extends AWxMpMsgCbController {
  @Override
  protected ReplyMsgData processSubscribeEvent(EventMsgData eventMsgData) throws BusinessException {
      // 用户关注了
  }
}
```
2. 重写processXXXMsg方法来处理关注的事件
3. 在微信公众号后台配置这个完整回调地址

### * 接入微信支付回调接口方法
1. 新增一个controller并继承AWeixinPayCbController，在类(非方法)上加上注解`@RequestMapping("/....")`作为微信支付的回调地址
```java
@Controller
@RequestMapping("/api/notice/pmall/wxpay/{appId}/callback")
public class WeixinPayCbController extends AWxPayCbController {
    @Override
    public void paySuccess(PayCallbackDto payCallbackDto) throws BusinessException {
        //支付回调校验完成，且支付成功了
    }
}
```
2. 实现paySuccess方法来处理支付成功后的业务逻辑
3. 在微信商户后台配置这个完整回调地址

### * 微信微信公众平台配置
1. 设置与开发->基本配置
- 可获取公众号的AppID、AppSecret、
- 配置IP白名单
- 配置服务器回调地址和消息加解密相关信息
- 绑定开放平台账号用来获取unionId
2. 如果需要开发微信内访问的H5页面，设置与开发->公众号设置->功能设置
- 配置业务域名，自己开发的H5在微信内部访问时不会出现风险提示
- 配置JS接口安全域名，如果自己开发的H5页面需要用到JSSDK，需要配置这个域名
- 配置网页授权域名，在这个域名下的URL可以通过微信oauth2的地址转发来获取用户授权code
    * 微信授权URL https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
    * 自己开发的H5页面会收到 REDIRECT_URI?code=CODE&state=STATE 的请求
    * 注意：只有当scope为"snsapi_userinfo"时服务端使用code换取信息才会返回unionid
    * 注意：REDIRECT_URI必须要encodeURI编码

### * 微信支付配置（商户平台）
1. TODO

### * 微信开放平台
1. 网站开发(不在微信内打开)
- 方式一：网页授权方式
  * 微信授权二维码 https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect
  * 自己开发的H5页面会收到 REDIRECT_URI?code=CODE&state=STATE 的请求
- 方式二：二维码授权方式
  * 引入weixinJS文件http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js
  * 使用weixinJS文件提供的对象方法生成二维码var obj = new WxLogin({...});
