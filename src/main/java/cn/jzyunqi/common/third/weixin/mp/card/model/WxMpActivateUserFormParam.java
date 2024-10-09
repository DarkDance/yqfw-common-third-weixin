package cn.jzyunqi.common.third.weixin.mp.card.model;

import cn.jzyunqi.common.third.weixin.mp.card.enums.FieldId;
import cn.jzyunqi.common.third.weixin.mp.card.enums.FieldType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author wiiyaya
 * @since 2024/10/9
 */
@Getter
@Setter
@ToString
public class WxMpActivateUserFormParam {

    /**
     * 卡券ID
     */
    @JsonProperty("card_id")
    private String cardId;

    /**
     * 会员卡守则
     */
    @JsonProperty("service_statement")
    private LinkInfo serviceStatement;

    /**
     * 绑定老会员链接
     */
    @JsonProperty("bind_old_card")
    private LinkInfo bindOldCard;

    /**
     * 必填项
     */
    @JsonProperty("required_form")
    private FormInfo requiredForm;

    /**
     * 可选项
     */
    @JsonProperty("optional_form")
    private FormInfo optionalForm;

    @Getter
    @Setter
    @ToString
    public static class LinkInfo {
        private String name;
        private String url;
    }

    @Getter
    @Setter
    @ToString
    public static class FormInfo {
        /**
         * 当前结构（required_form或者optional_form ）内的字段是否允许用户激活后再次修改，
         * 商户设置为true 时，需要接收相应事件通知处理修改事件
         */
        @JsonProperty("can_modify")
        private boolean canModify;

        /**
         * 富文本类型字段列表
         */
        @JsonProperty("rich_field_list")
        private List<FieldInfo> richFieldList;

        /**
         * 文本选项类型列表
         */
        @JsonProperty("custom_field_list")
        private List<String> customFieldList;


        /**
         * 微信格式化的选项类型
         */
        @JsonProperty("common_field_id_list")
        private List<FieldId> wechatFieldIdList;
    }

    @Getter
    @Setter
    @ToString
    public static class FieldInfo {
        /**
         * 富文本类型
         */
        @JsonProperty("type")
        private FieldType type;

        /**
         * 字段名
         */
        @JsonProperty("name")
        private String name;

        /**
         * 选择项
         */
        @JsonProperty("values")
        private List<String> valueList;
    }
}
