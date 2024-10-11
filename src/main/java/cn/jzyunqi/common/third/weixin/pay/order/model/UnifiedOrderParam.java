package cn.jzyunqi.common.third.weixin.pay.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

/**
 * @author wiiyaya
 * @since 2021/7/20.
 */
@Getter
@Setter
public class UnifiedOrderParam {

    @JsonProperty("appid")
    private String appId;

    @JsonProperty("mchid")
    private String mchId;

    private String description;

    private String outTradeNo;

    private ZonedDateTime timeExpire;

    private String notifyUrl;

    private PayAmountData amount = new PayAmountData();

    private PayPayerData payer = new PayPayerData();
}
