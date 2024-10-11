package cn.jzyunqi.common.third.weixin.pay.cert.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * @author wiiyaya
 * @since 2021/7/11.
 */
@Getter
@Setter
@ToString
public class PlantCertData {

    private String serialNo;

    private OffsetDateTime effectiveTime;

    private OffsetDateTime expireTime;

    private EncryptCertificate encryptCertificate;

    @Getter
    @Setter
    @ToString
    public static class EncryptCertificate{

        private String algorithm;

        private String nonce;

        private String associatedData;

        @JsonProperty("ciphertext")
        private String cipherText;
    }
}
