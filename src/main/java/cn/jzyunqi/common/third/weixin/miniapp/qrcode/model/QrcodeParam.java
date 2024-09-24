package cn.jzyunqi.common.third.weixin.miniapp.qrcode.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2021/6/3.
 */
@Getter
@Setter
public class QrcodeParam implements Serializable {
    @Serial
    private static final long serialVersionUID = -4586208528081232902L;

    /**
     * 值会作为 query 参数传递给小程序/小游戏
     */
    private String scene;

    /**
     * 小程序存在的页面，例如 pages/index/index
     */
    private String page;

    /**
     * 二维码的宽度，单位 px，最小 280px，最大 1280px
     */
    private Integer width;

    /**
     * 自动配置线条颜色
     */
    @JsonProperty("auto_color")
    private Boolean autoColor;

    /**
     * 使用 rgb 设置颜色
     */
    @JsonProperty("line_color")
    private LineColorData lineColor;

    /**
     * 是否需要透明底色
     */
    @JsonProperty("is_hyaline")
    private Boolean hyAline;
}
