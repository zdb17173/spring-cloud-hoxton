package org.fran.springcloud.config.client;

import org.fran.springcloud.dao.DaoLibrary;
import org.fran.springcloud.dao.mapper.AuthUserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author fran
 * @Description
 * @Date 2021/3/12 17:40
 */
@SpringBootApplication(scanBasePackageClasses = {ConfigClientApp.class, DaoLibrary.class})
@RestController
public class ConfigClientApp {
    @Value("${spring.datasource.url}")
    String name = "World";
    @Resource
    AuthUserMapper authUserMapper;

    @RequestMapping("/")
    public String home() {
        List<Map> map = authUserMapper.select();
        System.out.println(map);
        return "Hello " + name;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApp.class, args);
    }

}
