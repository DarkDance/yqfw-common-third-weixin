package cn.jzyunqi.common.third.weixin.mp.template.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

/**
 * @author wiiyaya
 * @since 2024/9/26
 * 将{"first_class":"xxx","second_class":"xxx"}转换为枚举值
 */
public class WxMpIndustryDeserializer extends JsonDeserializer<WxMpIndustryEnum> {
    @Override
    public WxMpIndustryEnum deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        if (!parser.isExpectedStartObjectToken()) {
            throw JsonMappingException.from(parser, "Expected an object but got " + parser.getCurrentToken());
        }
        parser.nextToken();
        String firstClass = parser.getText().trim();

        parser.nextToken();
        String secondClass = parser.getText().trim();

        return WxMpIndustryEnum.valueOf(firstClass, secondClass);
    }
}
