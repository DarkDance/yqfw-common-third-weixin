package cn.jzyunqi.common.third.weixin.client.interceptor;

import cn.jzyunqi.common.third.weixin.client.WeixinPayV3Client;
import cn.jzyunqi.common.utils.IOUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author wiiyaya
 * @since 2021/7/18.
 */
public class WeixinPayV3HeaderInterceptor implements ClientHttpRequestInterceptor {

    private final WeixinPayV3Client weixinPayV3Client;

    private final List<String> whitelist;

    public WeixinPayV3HeaderInterceptor(WeixinPayV3Client weixinPayV3Client, List<String> whitelist) {
        this.weixinPayV3Client = weixinPayV3Client;
        this.whitelist = whitelist;
    }

    @Override
    public @NonNull ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, @NonNull ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);

        if (!whitelist.contains(request.getURI().getPath()) && response.getStatusCode() == HttpStatus.OK) {
            Map<String, String> returnHeaderMap = response.getHeaders().toSingleValueMap();
            String bodyS = IOUtilPlus.toString(response.getBody(), StringUtilPlus.UTF_8);
            weixinPayV3Client.verifyHeader(returnHeaderMap, bodyS);
        }
        return response;
    }
}
