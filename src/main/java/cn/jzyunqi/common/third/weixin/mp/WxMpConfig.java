package cn.jzyunqi.common.third.weixin.mp;

import cn.jzyunqi.common.third.weixin.common.WxHttpExchangeWrapper;
import cn.jzyunqi.common.third.weixin.common.utils.WxFormatUtils;
import cn.jzyunqi.common.third.weixin.mp.card.WxMpCardApi;
import cn.jzyunqi.common.third.weixin.mp.card.WxMpCardApiProxy;
import cn.jzyunqi.common.third.weixin.mp.kefu.WxMpKefuApi;
import cn.jzyunqi.common.third.weixin.mp.kefu.WxMpKfApiProxy;
import cn.jzyunqi.common.third.weixin.mp.mass.WxMpMassApi;
import cn.jzyunqi.common.third.weixin.mp.mass.WxMpMassApiProxy;
import cn.jzyunqi.common.third.weixin.mp.material.WxMpMaterialApi;
import cn.jzyunqi.common.third.weixin.mp.material.WxMpMaterialApiProxy;
import cn.jzyunqi.common.third.weixin.mp.menu.WxMpMenuApi;
import cn.jzyunqi.common.third.weixin.mp.menu.WxMpMenuApiProxy;
import cn.jzyunqi.common.third.weixin.mp.subscribe.WxMpSubscribeMsgApi;
import cn.jzyunqi.common.third.weixin.mp.subscribe.WxMpSubscribeMsgApiProxy;
import cn.jzyunqi.common.third.weixin.mp.template.WxMpTemplateMsgApi;
import cn.jzyunqi.common.third.weixin.mp.template.WxMpTemplateMsgApiProxy;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApi;
import cn.jzyunqi.common.third.weixin.mp.token.WxMpTokenApiProxy;
import cn.jzyunqi.common.third.weixin.mp.user.WxMpUserApi;
import cn.jzyunqi.common.third.weixin.mp.user.WxMpUserApiProxy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        WebClient webClient = webClientBuilder.clone().codecs(WxFormatUtils::jackson2Config).build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpTokenApiProxy.class);
    }

    @Bean
    public WxMpTokenApi wxMpTokenApi() {
        return new WxMpTokenApi();
    }

    @Bean
    public WxMpKfApiProxy wxMpKfApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().codecs(WxFormatUtils::jackson2Config).build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpKfApiProxy.class);
    }

    @Bean
    public WxMpKefuApi wxMpKefuApi() {
        return new WxMpKefuApi();
    }

    @Bean
    public WxMpMenuApiProxy wxMpMenuApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().codecs(WxFormatUtils::jackson2Config).build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpMenuApiProxy.class);
    }

    @Bean
    public WxMpMenuApi wxMpMenuApi() {
        return new WxMpMenuApi();
    }

    @Bean
    public WxMpMaterialApiProxy wxMpMaterialApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().codecs(WxFormatUtils::jackson2ConfigSpecial).build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(30));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpMaterialApiProxy.class);
    }

    @Bean
    public WxMpMaterialApi wxMpMaterialApi() {
        return new WxMpMaterialApi();
    }

    @Bean
    public WxMpUserApiProxy wxMpUserApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().codecs(WxFormatUtils::jackson2Config).build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpUserApiProxy.class);
    }

    @Bean
    public WxMpUserApi wxMpUserApi() {
        return new WxMpUserApi();
    }

    @Bean
    public WxMpMassApiProxy wxMpMassApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().codecs(WxFormatUtils::jackson2Config).build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpMassApiProxy.class);
    }

    @Bean
    public WxMpMassApi wxMpMassApi() {
        return new WxMpMassApi();
    }

    @Bean
    public WxMpSubscribeMsgApiProxy wxMpSubscribeMsgApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpSubscribeMsgApiProxy.class);
    }

    @Bean
    public WxMpSubscribeMsgApi wxMpSubscribeMsgApi() {
        return new WxMpSubscribeMsgApi();
    }

    @Bean
    public WxMpTemplateMsgApiProxy wxMpTemplateMsgApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().codecs(WxFormatUtils::jackson2Config).build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpTemplateMsgApiProxy.class);
    }

    @Bean
    public WxMpTemplateMsgApi wxMpTemplateMsgApi() {
        return new WxMpTemplateMsgApi();
    }

    @Bean
    public WxMpCardApiProxy wxMpCardApiProxy(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.clone().codecs(WxFormatUtils::jackson2Config).build();
        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        webClientAdapter.setBlockTimeout(Duration.ofSeconds(5));
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return factory.createClient(WxMpCardApiProxy.class);
    }

    @Bean
    public WxMpCardApi wxMpCardApi() {
        return new WxMpCardApi();
    }
}
