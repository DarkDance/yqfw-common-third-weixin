package cn.jzyunqi.common.third.weixin.open;

import cn.jzyunqi.common.third.weixin.common.WxHttpExchangeWrapper;
import cn.jzyunqi.common.third.weixin.common.utils.WxFormatUtils;
import cn.jzyunqi.common.third.weixin.open.user.WxOpenUserApiProxy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@Configuration
public class WxOpenConfig {

    @Bean
    @ConditionalOnMissingBean
    public WxHttpExchangeWrapper responseCheckWrapper() {
        return new WxHttpExchangeWrapper();
    }

    @Bean
    public WxOpenClient wxOpenClient() {
        return new WxOpenClient();
    }

    @Bean
    public WxOpenUserApiProxy wxOpenUserApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().codecs(WxFormatUtils::jackson2ConfigSpecial).build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxOpenUserApiProxy.class);
    }
}
