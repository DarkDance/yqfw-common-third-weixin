package cn.jzyunqi.common.third.weixin.mp.menu.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wiiyaya
 * @since 2018/8/22.
 */
@Getter
@Setter
public class WxMenuRuleData {
    /**
     * 组id
     */
    private Integer tagId;

    /**
     * 性别，男（1）女（2）
     */
    private String sex;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 平台，IOS(1), Android(2),Others(3)
     */
    private String clientPlatformType;
}
