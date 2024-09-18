package cn.jzyunqi.common.third.weixin.model.callback.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2021/7/21.
 */
@Getter
@Setter
public class PayResultData implements Serializable {
    @Serial
    private static final long serialVersionUID = -1248956564460889857L;

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
