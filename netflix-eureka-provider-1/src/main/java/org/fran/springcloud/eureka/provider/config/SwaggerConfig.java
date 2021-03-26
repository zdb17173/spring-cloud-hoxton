package org.fran.springcloud.eureka.provider.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Value("${swagger.host}")
    String host;
    @Value("${swagger.title}")
    String title;

    @Bean
    public Docket swaggerSpringFoxDocket() {
        String packagePath = this.getClass().getPackage().getName();
        packagePath = packagePath.substring(0, packagePath.lastIndexOf("."));
//        packagePath = packagePath.substring(0, packagePath.lastIndexOf("."));

        StopWatch watch = new StopWatch();
        watch.start();
        Docket swaggerSpringMvcPlugin = new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage(packagePath))
                .paths(PathSelectors.any())
                .build();
        watch.stop();
        return swaggerSpringMvcPlugin;
    }

    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title(title)
                .description("")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}