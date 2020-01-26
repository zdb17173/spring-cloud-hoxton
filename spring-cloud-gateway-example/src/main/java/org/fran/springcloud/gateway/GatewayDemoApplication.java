package org.fran.springcloud.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author fran
 * @Description
 * @Date 2020/1/26 11:41
 */
@SpringBootApplication
public class GatewayDemoApplication {
    public static void main(String[] args){
        SpringApplication.run( GatewayDemoApplication.class, args );
    }
}
