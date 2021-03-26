package org.fran.springcloud.eureka.feign.config;

import feign.*;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class FeignConfig {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;


    /*
    可以采用以下方式来设置encoder和日志级别，也可以采用Builder方式整体设置所有配置
    @Bean
    @Primary
    @Scope("prototype")
    public Encoder multipartFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public feign.Logger.Level multipartLoggerLevel() {
        return feign.Logger.Level.FULL;
    }*/


    @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        Feign.Builder b = Feign.builder()
                //请求拦截器，方便请求其他服务时增加token等全局变量
                .requestInterceptor(new RequestInterceptor() {
                    @Override
                    public void apply(RequestTemplate requestTemplate) {
                        requestTemplate.header("fran", "dsadsada");
                    }
                })
                //连接设置 connectTimeoutMillis设置请求超时; readTimeoutMillis连接超时; followRedirects自动重定向.
                // 默认10秒连接超时; 60秒请求超时; 自动重定向;
                .options(new Request.Options(
                        10,
//                        TimeUnit.SECONDS,
                        60,
//                        TimeUnit.SECONDS,
                        true
                ))

                //重试设置 默认5次重试
                .retryer(new Retryer.Default())

                //支持POST请求、支持multipart请求
                .encoder(new SpringFormEncoder(new SpringEncoder(messageConverters)))

                //全日志
                .logLevel(Logger.Level.FULL);

        return b;
    }

    @Autowired
    public void configCli(Client client) {
        LoadBalancerFeignClient c = (LoadBalancerFeignClient) client;
    }
}
