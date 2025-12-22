package cn.jzyunqi.common.third.weixin.mp.template.enums;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DatabindException;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

/**
 * @author wiiyaya
 * @since 2024/9/26
 * 将{"first_class":"xxx","second_class":"xxx"}转换为枚举值
 */
public class WxMpIndustryDeserializer extends ValueDeserializer<WxMpIndustryEnum> {

    @Override
    public WxMpIndustryEnum deserialize(JsonParser parser, DeserializationContext deserializationContext) throws JacksonException {
        if (!parser.isExpectedStartObjectToken()) {
            throw DatabindException.from(parser, "Expected an object but got " + parser.currentToken());
        }
        parser.nextToken();
        String firstClass = parser.getString().trim();

        parser.nextToken();
        String secondClass = parser.getString().trim();

        return WxMpIndustryEnum.valueOf(firstClass, secondClass);
    }
}
