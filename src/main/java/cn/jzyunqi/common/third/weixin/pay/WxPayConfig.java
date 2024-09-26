package cn.jzyunqi.common.third.weixin.pay;

import cn.jzyunqi.common.third.weixin.common.WxHttpExchangeWrapper;
import cn.jzyunqi.common.third.weixin.pay.order.WxPayOrderApiProxy;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

import javax.net.ssl.SSLException;
import java.time.Duration;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@Configuration
@Slf4j
public class WxPayConfig {

    @Bean
    @ConditionalOnMissingBean
    public WxHttpExchangeWrapper responseCheckWrapper() {
        return new WxHttpExchangeWrapper();
    }

    @Bean
    public WxPayClient wxPayClient() {
        return new WxPayClient();
    }

    @Bean
    public WxPayOrderApiProxy wxPayOrderApiProxy(WebClient.Builder webClientBuilder, WxPayClient wxPayClient) {
        WebClient webClient = webClientBuilder.clone()
                .filter(ExchangeFilterFunction.ofRequestProcessor(request -> {
                    ClientRequest filtered = ClientRequest.from(request)
                            .header("Authorization", wxPayClient.headerSign(request.method(), request.url().getPath(), request.body().toString()))
                            .build();
                    return Mono.just(filtered);
                }))
                .filter((request, next) -> next.exchange(request).flatMap(response -> DataBufferUtils.join(response.bodyToFlux(DataBuffer.class))
                        .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            String responseBody = new String(bytes, StringUtilPlus.UTF_8);
                            log.debug("response body: {}", responseBody);
                            try {
                                wxPayClient.verifyHeader(response.headers().asHttpHeaders().toSingleValueMap(), responseBody);
                                return response.mutate().build();
                            } catch (SSLException e) {
                                throw new RuntimeException(e);
                            }
                        })))
                .build();

        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxPayOrderApiProxy.class);
    }
}
