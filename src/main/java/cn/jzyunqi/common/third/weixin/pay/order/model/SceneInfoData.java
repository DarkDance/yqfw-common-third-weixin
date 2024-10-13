package cn.jzyunqi.common.third.weixin.pay.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2024/10/13
 */
@Getter
@Setter
@ToString
public class SceneInfoData {

    /**
     * <pre>
     * 字段名：用户终端IP
     * 变量名：payer_client_ip
     * 是否必填：是
     * 类型：string[1,45]
     * 描述：
     *  用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。
     *  示例值：14.23.150.211
     * </pre>
     */
    private String payerClientIp;
    /**
     * <pre>
     * 字段名：商户端设备号
     * 变量名：device_id
     * 是否必填：否
     * 类型：string[1,32]
     * 描述：
     *  商户端设备号（门店号或收银设备ID）。
     *  示例值：013467007045764
     * </pre>
     */
    private String deviceId;
    /**
     * <pre>
     * 字段名：商户门店信息
     * 变量名：store_info
     * 是否必填：否
     * 类型：object
     * 描述：
     *  商户门店信息
     * </pre>
     */
    private StoreInfo storeInfo;

    /**
     * <pre>
     * 字段名：H5场景信息
     * 变量名：h5_info
     * 是否必填：否(H5支付必填)
     * 类型：object
     * 描述：
     *  H5场景信息
     * </pre>
     */
    private StoreInfo h5Info;

    @Getter
    @Setter
    @ToString
    public static class StoreInfo {
        /**
         * <pre>
         * 字段名：门店编号
         * 变量名：id
         * 是否必填：是
         * 类型：string[1,32]
         * 描述：
         *  商户侧门店编号
         *  示例值：0001
         * </pre>
         */
        private String id;
        /**
         * <pre>
         * 字段名：门店名称
         * 变量名：name
         * 是否必填：否
         * 类型：string[1,256]
         * 描述：
         *  商户侧门店名称
         *  示例值：腾讯大厦分店
         * </pre>
         */
        private String name;
        /**
         * <pre>
         * 字段名：地区编码
         * 变量名：area_code
         * 是否必填：否
         * 类型：string[1,32]
         * 描述： 地区编码, <a href="https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/ecommerce/applyments/chapter4_1.shtml">详细请见省市区编号对照表</a>。
         * 示例值：440305
         * </pre>
         */
        private String areaCode;
        /**
         * <pre>
         * 字段名：详细地址
         * 变量名：address
         * 是否必填：是
         * 类型：string[1,512]
         * 描述：
         *  详细的商户门店地址
         *  示例值：广东省深圳市南山区科技中一道10000号
         * </pre>
         */
        private String address;
    }

    @Getter
    @Setter
    @ToString
    public static class H5Info {
        /**
         * <pre>
         * 字段名：场景类型
         * 变量名：type
         * 是否必填：是
         * 类型：string[1,32]
         * 描述：
         *  场景类型
         *  示例值：iOS, Android, Wap
         * </pre>
         */
        private String type;
        /**
         * <pre>
         * 字段名：应用名称
         * 变量名：app_name
         * 是否必填：否
         * 类型：string[1,64]
         * 描述：
         *  应用名称
         *  示例值：王者荣耀
         * </pre>
         */
        private String appName;
        /**
         * <pre>
         * 字段名：网站URL
         * 变量名：app_url
         * 是否必填：否
         * 类型：string[1,128]
         * 描述：
         *  网站URL
         *  示例值：https://pay.qq.com
         * </pre>
         */
        private String appUrl;
        /**
         * <pre>
         * 字段名：iOS平台BundleID
         * 变量名：bundle_id
         * 是否必填：否
         * 类型：string[1,128]
         * 描述：
         *  iOS平台BundleID
         *  示例值：com.tencent.wzryiOS
         * </pre>
         */
        private String bundleId;
        /**
         * <pre>
         * 字段名：Android平台PackageName
         * 变量名：package_name
         * 是否必填：否
         * 类型：string[1,128]
         * 描述：
         *  Android平台PackageName
         *  示例值：com.tencent.tmgp.sgame
         * </pre>
         */
        private String packageName;
    }
}
