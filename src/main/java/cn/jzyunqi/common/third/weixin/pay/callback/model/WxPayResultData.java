package cn.jzyunqi.common.third.weixin.pay.callback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2021/7/21.
 */
@Getter
@Setter
@ToString
public class WxPayResultData {

    /**
     * 加密算法类型
     */
    private String algorithm;

    /**
     * 数据密文
     */
    @JsonProperty("ciphertext")
    private String cipherText;

    /**
     * 附加数据
     */
    @JsonProperty("associated_data")
    private String associatedData;

    /**
     * 原始类型
     */
    @JsonProperty("original_type")
    private String originalType;

    /**
     * 随机串
     */
    private String nonce;
}
