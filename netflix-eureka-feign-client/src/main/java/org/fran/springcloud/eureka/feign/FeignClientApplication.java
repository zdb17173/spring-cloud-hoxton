package org.fran.springcloud.eureka.feign;

import org.fran.springcloud.eureka.feign.config.FeignConfig;
import org.fran.springcloud.eureka.feign.config.FileToMultipartFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

/**
 * @author fran
 * @Description
 * @Date 2020/9/14 19:19
 */
@SpringBootApplication
@EnableFeignClients
@RestController
public class FeignClientApplication {

    @Resource
    HelloClient helloClient;

    @RequestMapping("/")
    public Result hello() {

        Result r = helloClient.echo();

        RemoveConfigParam p = new RemoveConfigParam();
        p.setChannel("sda");
        p.setKey("key");
        p.setOs("os");
        Result<String> r2 = helloClient.post(p);

        Result r3 = helloClient.upload(FileToMultipartFile.getMulFile(
                new File("C:\\temp\\test.jpg")),
                "description",
                "name"
        );

        return r2;
    }

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApplication.class, args);
    }


    @FeignClient(name = "ribbon-service", configuration = FeignConfig.class)
    interface HelloClient {
        @GetMapping(value = "/api/test/checkLogin", consumes = "application/json")
        public Result echo();

        @PostMapping(value = "/api/test/selectString", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
        public Result<String> post(@RequestBody RemoveConfigParam baseParam);

        @PostMapping(value = "/api/test/upload",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public Result upload(
                @RequestPart("uploadFile")MultipartFile uploadFile,
                @RequestPart("description")String description,
                @RequestPart("name")String name);
    }


    public class RemoveConfigParam {
        private int removeType;
        private String key;
        private String version;
        private String os;
        private String channel;

        public int getRemoveType() {
            return removeType;
        }

        public void setRemoveType(int removeType) {
            this.removeType = removeType;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }
    }

    public static class Result<T> {
        private int status;
        private String description;
        private T data;

        public Result(){}

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
