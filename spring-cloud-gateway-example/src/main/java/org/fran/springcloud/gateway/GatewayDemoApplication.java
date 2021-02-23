package org.fran.springcloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.gateway.route.InMemoryRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author fran
 * @Description
 * @Date 2020/1/26 11:41
 */
@SpringBootApplication
@EnableEurekaClient
public class GatewayDemoApplication {

    @Bean
    public RouteDefinitionWriter routeDefinitionWriter() {
        return new InMemoryRouteDefinitionRepository();
    }

    @Bean
    public MysqlRouteDefinitionRepository mysqlRouteDefinitionRepository() {
        return new MysqlRouteDefinitionRepository();
    }

    public static void main(String[] args){
        SpringApplication.run(GatewayDemoApplication.class, args);
    }
}
