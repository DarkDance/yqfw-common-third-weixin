package cn.jzyunqi.common.third.weixin.model.request.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2018/8/25.
 */
@Getter
@Setter
public class MassFilterData {

    @JsonProperty("is_to_all")
    private Boolean isToAll;

    @JsonProperty("tag_id")
    private String tagId;
}
