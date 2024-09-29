package cn.jzyunqi.common.third.weixin.mp;

import cn.jzyunqi.common.third.weixin.common.WxHttpExchangeWrapper;
import cn.jzyunqi.common.third.weixin.mp.kefu.WxMpKfApiProxy;
import cn.jzyunqi.common.third.weixin.mp.mass.WxMpMassApiProxy;
import cn.jzyunqi.common.third.weixin.mp.material.WxMpMaterialApiProxy;
import cn.jzyunqi.common.third.weixin.mp.menu.WxMpMenuApiProxy;
import cn.jzyunqi.common.third.weixin.mp.subscribe.WxMpSubscribeMsgApiProxy;
import cn.jzyunqi.common.third.weixin.mp.template.WxMpTemplateMsgApiProxy;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApiProxy;
import cn.jzyunqi.common.third.weixin.mp.user.WxMpUserApiProxy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

/**
 * @author wiiyaya
 * @since 2024/9/24
 */
@Configuration
public class WxMpConfig {

    @Bean
    @ConditionalOnMissingBean
    public WxHttpExchangeWrapper wxHttpExchangeWrapper() {
        return new WxHttpExchangeWrapper();
    }
    @Bean
    public WxMpClient wxMpClient() {
        return new WxMpClient();
    }

    @Bean
    public WxMpTokenApiProxy wxMpTokenApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpTokenApiProxy.class);
    }

    @Bean
    public WxMpKfApiProxy wxMpKfApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpKfApiProxy.class);
    }

    @Bean
    public WxMpMenuApiProxy wxMpMenuApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpMenuApiProxy.class);
    }

    @Bean
    public WxMpMaterialApiProxy wxMpMaterialApiProxy(WebClient.Builder webClientBuilder, Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        WebClient webClient = webClientBuilder.clone()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .jackson2JsonDecoder(new Jackson2JsonDecoder(jackson2ObjectMapperBuilder.build(),
                                MediaType.APPLICATION_JSON,
                                MediaType.TEXT_PLAIN
                        )))
                .build();

        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpMaterialApiProxy.class);
    }

    @Bean
    public WxMpUserApiProxy wxMpUserApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpUserApiProxy.class);
    }

    @Bean
    public WxMpMassApiProxy wxMpMassApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpMassApiProxy.class);
    }

    @Bean
    public WxMpSubscribeMsgApiProxy wxMpSubscribeMsgApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpSubscribeMsgApiProxy.class);
    }

    @Bean
    public WxMpTemplateMsgApiProxy wxMpTemplateMsgApiProxy(WebClient.Builder webClientBuilder) {
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClientBuilder.build());
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpTemplateMsgApiProxy.class);
    }
}
