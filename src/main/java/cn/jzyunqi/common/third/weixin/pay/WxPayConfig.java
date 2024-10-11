package cn.jzyunqi.common.third.weixin.pay;

import cn.jzyunqi.common.third.weixin.common.WxHttpExchangeWrapper;
import cn.jzyunqi.common.third.weixin.pay.cert.WxPayCertApiProxy;
import cn.jzyunqi.common.third.weixin.common.utils.AuthUtils;
import cn.jzyunqi.common.third.weixin.pay.order.WxPayOrderApiProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    @ConditionalOnMissingBean
    public WxHttpExchangeWrapper responseCheckWrapper() {
        return new WxHttpExchangeWrapper();
    }

    @Bean(WxPayStrange.ID)
    public WxPayStrange wxPayStrange() {
        return new WxPayStrange();
    }

    @Bean
    public WxPayClient wxPayClient() {
        return new WxPayClient();
    }

    @Bean
    public WxPayOrderApiProxy wxPayOrderApiProxy(WebClient.Builder webClientBuilder, WxPayClientConfig wxPayClientConfig) {
        WebClient webClient = webClientBuilder.clone()
                .filter(ExchangeFilterFunction.ofRequestProcessor(request -> {
                    ClientRequest filtered = ClientRequest.from(request)
                            .header("Authorization",
                                    AuthUtils.genAuthToken(
                                            wxPayClientConfig.getMerchantId(),
                                            wxPayClientConfig.getMerchantSerialNumber(),
                                            wxPayClientConfig.getMerchantPrivateKey(),
                                            request.method(),
                                            request.url().getPath(),
                                            request.body().toString()
                                    )
                            )
                            .build();
                    return Mono.just(filtered);
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
                .filter(ExchangeFilterFunction.ofRequestProcessor(request -> {
                    ClientRequest filtered = ClientRequest.from(request)
                            .header("Authorization",
                                    AuthUtils.genAuthToken(
                                            wxPayClientConfig.getMerchantId(),
                                            wxPayClientConfig.getMerchantSerialNumber(),
                                            wxPayClientConfig.getMerchantPrivateKey(),
                                            request.method(),
                                            request.url().getPath(),
                                            request.body().toString()
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
