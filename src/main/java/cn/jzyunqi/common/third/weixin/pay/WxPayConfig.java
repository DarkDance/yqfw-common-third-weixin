package cn.jzyunqi.common.third.weixin.pay;

import cn.jzyunqi.common.third.weixin.common.WxHttpExchangeWrapper;
import cn.jzyunqi.common.third.weixin.common.utils.AuthUtils;
import cn.jzyunqi.common.third.weixin.common.utils.WxFormatUtils;
import cn.jzyunqi.common.third.weixin.pay.cert.WxPayCertApiProxy;
import cn.jzyunqi.common.third.weixin.pay.order.WxPayOrderApiProxy;
import cn.jzyunqi.common.utils.StringUtilPlus;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@Configuration
@Slf4j
public class WxPayConfig {

    private static final String HEADER_AUTHORIZATION = "Authorization";

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
    public WxPayOrderApiProxy wxPayOrderApiProxy(WebClient.Builder webClientBuilder, WxPayClientConfig wxPayClientConfig) {
        WebClient webClient = webClientBuilder.clone()
                .codecs(WxFormatUtils::jackson2Config)
                .filter(ExchangeFilterFunction.ofRequestProcessor(request -> {
                    ClientRequest.Builder amendRequest = ClientRequest.from(request);
                    if(request.method() == HttpMethod.GET){
                        amendRequest.header(HEADER_AUTHORIZATION, AuthUtils.genAuthToken(
                                wxPayClientConfig.getMerchantId(),
                                wxPayClientConfig.getMerchantSerialNumber(),
                                wxPayClientConfig.getMerchantPrivateKey(),
                                request.method(),
                                request.url().getPath() + "?" + request.url().getQuery(),
                                null
                        ));
                    }else{
                        amendRequest.body((outputMessage, context) -> request.body().insert(new ClientHttpRequestDecorator(outputMessage) {
                            @Override
                            public @NonNull Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                                return DataBufferUtils.join(body).flatMap(buffer -> {
                                    String bodyStr = buffer.toString(StringUtilPlus.UTF_8);
                                    getHeaders().add(HEADER_AUTHORIZATION,
                                            AuthUtils.genAuthToken(
                                                    wxPayClientConfig.getMerchantId(),
                                                    wxPayClientConfig.getMerchantSerialNumber(),
                                                    wxPayClientConfig.getMerchantPrivateKey(),
                                                    request.method(),
                                                    request.url().getPath(),
                                                    bodyStr
                                            ));
                                    return super.writeWith(Mono.just(buffer));
                                });
                            }
                        }, context));
                    }
                    return Mono.just(amendRequest.build());
                }))
                .build();

        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxPayOrderApiProxy.class);
    }

    @Bean
    public WxPayCertApiProxy wxPayCertApiProxy(WebClient.Builder webClientBuilder, WxPayClientConfig wxPayClientConfig) {
        WebClient webClient = webClientBuilder.clone()
                .codecs(WxFormatUtils::jackson2Config)
                .filter(ExchangeFilterFunction.ofRequestProcessor(request -> {
                    ClientRequest filtered = ClientRequest.from(request)
                            .header(HEADER_AUTHORIZATION,
                                    AuthUtils.genAuthToken(
                                            wxPayClientConfig.getMerchantId(),
                                            wxPayClientConfig.getMerchantSerialNumber(),
                                            wxPayClientConfig.getMerchantPrivateKey(),
                                            request.method(),
                                            request.url().getPath(),
                                            null
                                    )
                            )
                            .build();
                    return Mono.just(filtered);
                }))
                .build();

        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxPayCertApiProxy.class);
    }
}
