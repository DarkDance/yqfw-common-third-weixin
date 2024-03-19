### 接入微信消息回调接口方法
1. 新增一个controller并继承AWeixinMsgCbController，在类(非方法)上加上注解`@RequestMapping("/....")`作为微信公众号的回调地址
2. 重写processXXXMsg方法来处理关注的事件
3. 在微信公众号后台配置这个完整回调地址

### 接入微信支付回调接口方法
1. 新增一个controller并继承AWeixinPayCbController，在类(非方法)上加上注解`@RequestMapping("/....")`作为微信支付的回调地址
2. 实现paySuccess方法来处理支付成功后的业务逻辑
3. 在微信商户后台配置这个完整回调地址