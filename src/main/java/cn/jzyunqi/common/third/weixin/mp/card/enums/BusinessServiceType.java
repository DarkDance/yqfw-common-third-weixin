package cn.jzyunqi.common.third.weixin.mp.card.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @since 2024/10/9
 */
@Getter
@AllArgsConstructor
public enum BusinessServiceType {
    BIZ_SERVICE_DELIVER("外卖服务"),
    BIZ_SERVICE_FREE_PARK("停车位"),
    BIZ_SERVICE_WITH_PET("可带宠物"),
    BIZ_SERVICE_FREE_WIFI("免费wifi");

    private final String description;
}
