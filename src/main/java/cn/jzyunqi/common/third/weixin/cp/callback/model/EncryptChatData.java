package cn.jzyunqi.common.third.weixin.cp.callback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author wiiyaya
 * @since 2025/12/2
 */
@Getter
@Setter
@ToString
public class EncryptChatData {

    /**
     * 消息的seq值，标识消息的序号。再次拉取需要带上上次回包中最大的seq。Uint64类型，范围0-pow(2,64)-1
     */
    private Long seq;

    /**
     * 消息id，消息的唯一标识，企业可以使用此字段进行消息去重
     * 注1：以_external结尾的消息，表明该消息是一条外部消息。
     * 注2：以_updown_stream结尾的消息，表明该消息是一条上下游消息。
     */
    @JsonProperty("msgid")
    private String msgId;

    /**
     * 加密此条消息使用的公钥版本号
     */
    @JsonProperty("publickey_ver")
    private Integer publicKeyVer;

    /**
     * 1. 使用publickey_ver确定私钥
     * 2. 使用私钥解密encrypt_random_key，得到随机密钥
     * 注：该字段使用base64编码
     */
    @JsonProperty("encrypt_random_key")
    private String encryptRandomKey;

    /**
     * 消息密文，使用随机密钥加密的消息密文。
     */
    @JsonProperty("encrypt_chat_msg")
    private String encryptChatMsg;
}
