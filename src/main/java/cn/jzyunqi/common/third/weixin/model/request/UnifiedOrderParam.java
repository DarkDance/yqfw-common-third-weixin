package cn.jzyunqi.common.third.weixin.model.request;

import cn.jzyunqi.common.third.weixin.model.PayAmountData;
import cn.jzyunqi.common.third.weixin.model.PayPayerData;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * @author wiiyaya
 * @date 2021/7/20.
 */
@Getter
@Setter
public class UnifiedOrderParam implements Serializable {
    private static final long serialVersionUID = -3549100768853004121L;

    @JsonProperty("appid")
    private String appId;

    @JsonProperty("mchid")
    private String mchId;

    private String description;

    private String outTradeNo;

    private OffsetDateTime timeExpire;

    private String notifyUrl;

    private PayAmountData amount = new PayAmountData();

    private PayPayerData payer = new PayPayerData();
}
