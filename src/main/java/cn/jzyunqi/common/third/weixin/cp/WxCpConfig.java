package cn.jzyunqi.common.third.weixin.cp;

import cn.jzyunqi.common.third.weixin.common.WxHttpExchangeWrapper;
import cn.jzyunqi.common.third.weixin.common.utils.WxFormatUtils;
import cn.jzyunqi.common.third.weixin.cp.token.WxCpTokenApi;
import cn.jzyunqi.common.third.weixin.cp.token.WxCpTokenApiProxy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

/**
 * @author wiiyaya
 * @since 2025/11/17
 */
@Configuration
public class WxCpConfig {

    @Bean
    @ConditionalOnMissingBean
    public WxHttpExchangeWrapper wxHttpExchangeWrapper() {
        return new WxHttpExchangeWrapper();
    }

    @Bean
    public WxCpClient wxCpClient() {
        return new WxCpClient();
    }

    @Bean
    public WxCpTokenApiProxy wxCpTokenApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().codecs(WxFormatUtils::jackson2Config).build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxCpTokenApiProxy.class);
    }

    @Bean
    public WxCpTokenApi wxCpTokenApi() {
        return new WxCpTokenApi();
    }

}
