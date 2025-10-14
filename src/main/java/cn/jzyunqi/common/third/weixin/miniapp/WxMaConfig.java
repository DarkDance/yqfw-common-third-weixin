package cn.jzyunqi.common.third.weixin.miniapp;

import cn.jzyunqi.common.third.weixin.common.WxHttpExchangeWrapper;
import cn.jzyunqi.common.third.weixin.miniapp.qrcode.WxMaQrcodeApi;
import cn.jzyunqi.common.third.weixin.miniapp.qrcode.WxMaQrcodeApiProxy;
import cn.jzyunqi.common.third.weixin.open.WxOpenClient;
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
public class WxMaConfig {

    @Bean
    @ConditionalOnMissingBean
    public WxHttpExchangeWrapper wxHttpExchangeWrapper() {
        return new WxHttpExchangeWrapper();
    }

    @Bean
    public WxMaClient wxMaClient() {
        return new WxMaClient();
    }

    @Bean
    public WxMaQrcodeApiProxy wxMaQrcodeApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMaQrcodeApiProxy.class);
    }

    @Bean
    public WxMaQrcodeApi wxMaQrcodeApi() {
        return new WxMaQrcodeApi();
    }
}
