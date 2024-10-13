package cn.jzyunqi.common.third.weixin.pay.order.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * @author wiiyaya
 * @since 2021/7/20.
 */
@Getter
@Setter
public class UnifiedOrderParam {
    /**
     * <pre>
     * 字段名：应用ID
     * 变量名：appid
     * 是否必填：是
     * 类型：string[1,32]
     * 描述：
     *  由微信生成的应用ID，全局唯一。请求统一下单接口时请注意APPID的应用属性，例如公众号场景下，需使用应用属性为公众号的APPID
     *  示例值：wxd678efh567hg6787
     * </pre>
     */
    @JsonProperty("appid")
    private String appId;
    /**
     * <pre>
     * 字段名：直连商户号
     * 变量名：mchid
     * 是否必填：是
     * 类型：string[1,32]
     * 描述：
     *  直连商户的商户号，由微信支付生成并下发。
     *  示例值：1230000109
     * </pre>
     */
    @JsonProperty("mchid")
    private String mchId;
    /**
     * <pre>
     * 字段名：商品描述
     * 变量名：description
     * 是否必填：是
     * 类型：string[1,127]
     * 描述：
     *  商品描述
     *  示例值：Image形象店-深圳腾大-QQ公仔
     * </pre>
     */
    private String description;
    /**
     * <pre>
     * 字段名：商户订单号
     * 变量名：out_trade_no
     * 是否必填：是
     * 类型：string[6,32]
     * 描述：
     *  商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一
     *  示例值：1217752501201407033233368018
     * </pre>
     */
    private String outTradeNo;
    /**
     * <pre>
     * 字段名：交易结束时间
     * 变量名：time_expire
     * 是否必填：是
     * 类型：string[1,64]
     * 描述：
     *  订单失效时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
     *  示例值：2018-06-08T10:34:56+08:00
     * </pre>
     */
    private ZonedDateTime timeExpire;
    /**
     * <pre>
     * 字段名：附加数据
     * 变量名：attach
     * 是否必填：否
     * 类型：string[1,128]
     * 描述：
     *  附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
     *  示例值：自定义数据
     * </pre>
     */
    private String attach;
    /**
     * <pre>
     * 字段名：通知地址
     * 变量名：notify_url
     * 是否必填：是
     * 类型：string[1,256]
     * 描述：
     *  通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。
     *  格式：URL
     *  示例值：https://www.weixin.qq.com/wxpay/pay.php
     * </pre>
     */
    private String notifyUrl;
    /**
     * <pre>
     * 字段名：订单优惠标记
     * 变量名：goods_tag
     * 是否必填：否
     * 类型：string[1,256]
     * 描述：
     *  订单优惠标记
     *  示例值：WXG
     * </pre>
     */
    private String goodsTag;
    /**
     * <pre>
     * 字段名：电子发票入口开放标识
     * 变量名：support_fapiao
     * 是否必填：否
     * 类型：boolean
     * 描述：传入true时，支付成功消息和支付详情页将出现开票入口。需要在微信支付商户平台或微信公众平台开通电子发票功能，传此字段才可生效。
     * </pre>
     */
    private String supportFapiao;
    /**
     * <pre>
     * 字段名：订单金额
     * 变量名：amount
     * 是否必填：是
     * 类型：object
     * 描述：
     *  订单金额信息
     * </pre>
     */
    private PayAmountData amount = new PayAmountData();
    /**
     * <pre>
     * 字段名：支付者
     * 变量名：payer
     * 是否必填：否(JSAPI、小程序支付必填)
     * 类型：object
     * 描述：
     *  支付者信息
     * </pre>
     */
    private PayPayerData payer = new PayPayerData();
    /**
     * <pre>
     * 字段名：优惠功能
     * 变量名：detail
     * 是否必填：否
     * 类型：object
     * 描述：
     *  优惠功能
     * </pre>
     */
    private DiscountData detail = new DiscountData();
    /**
     * <pre>
     * 字段名：场景信息
     * 变量名：scene_info
     * 是否必填：否
     * 类型：object
     * 描述：
     *  支付场景描述
     * </pre>
     */
    private SceneInfoData sceneInfo;
    /**
     * <pre>
     * 字段名：结算信息
     * 变量名：settle_info
     * 是否必填：否
     * 类型：Object
     * 描述：结算信息
     * </pre>
     */
    private SettleInfoData settleInfo;
}
