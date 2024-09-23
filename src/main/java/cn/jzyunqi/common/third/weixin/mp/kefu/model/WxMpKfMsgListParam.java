package cn.jzyunqi.common.third.weixin.mp.kefu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Getter
@Setter
public class WxMpKfMsgListParam {

    /**
     * 起始时间，unix时间戳
     */
    @JsonProperty("starttime")
    private Long startTime;

    /**
     * 结束时间，unix时间戳，每次查询时段不能超过24小时
     */
    @JsonProperty("endtime")
    private Long endTime;

    /**
     * 消息id顺序从小到大，从1开始
     */
    @JsonProperty("msgid")
    private Long msgId;

    /**
     * 每次获取条数，最多10000条
     */
    private Integer number;
}
