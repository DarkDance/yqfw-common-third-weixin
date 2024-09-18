package cn.jzyunqi.common.third.weixin.pay.model.response.item;

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
public class PlantCertData implements Serializable {
    @Serial
    private static final long serialVersionUID = 2607510178884489441L;

    @JsonProperty("serial_no")
    private String serialNo;

    @JsonProperty("effective_time")
    private OffsetDateTime effectiveTime;

    @JsonProperty("expire_time")
    private OffsetDateTime expireTime;

    @JsonProperty("encrypt_certificate")
    private EncryptCertificate encryptCertificate;

    @Getter
    @Setter
    @ToString
    public static class EncryptCertificate implements Serializable{
        @Serial
        private static final long serialVersionUID = -8766601908808238086L;

        private String algorithm;
        private String nonce;

        @JsonProperty("associated_data")
        private String associatedData;

        @JsonProperty("ciphertext")
        private String cipherText;
    }
}
