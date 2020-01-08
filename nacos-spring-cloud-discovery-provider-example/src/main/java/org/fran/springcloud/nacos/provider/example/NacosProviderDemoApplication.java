package org.fran.springcloud.nacos.provider.example;

import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosProviderDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosProviderDemoApplication.class, args);
    }

    @RestController
    public class EchoController {
        @GetMapping(value = "/echo/{string}")
        public Result echo(@PathVariable("string") String string, @RequestHeader("fran") String fran) {
            return new Result("Hello Nacos Discovery " + string +  " header[" + fran + "]");
        }

        @PostMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
        public Result<String> post(@RequestBody Param baseParam){
            Result<String> res = new Result<>();
            res.setData("dsadsad");
            res.setRes("[" + baseParam.getParam1() + "] ["+ baseParam.getParam2() +"]");
            res.setType(200);
            return res;
        }

        @PostMapping(value = "/upload",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
        public Result upload(
                @RequestPart("uploadFile")MultipartFile uploadFile,
                @RequestParam("description")String description,
                @RequestParam("name")String name){
            FileOutputStream o = null;
            InputStream inputStream = null;
            try {
                inputStream = uploadFile.getInputStream();
                File f = new File("C:\\temp\\aa.jpg");
                o = new FileOutputStream(f);
                byte[] b = new byte[1024];
                while(inputStream.read(b)!= -1){
                    o.write(b);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if(o!= null)
                        o.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if(inputStream!= null)
                        inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Result res = new Result();
            res.setData("success");
            res.setType(200);
            return res;
        }
    }



    public static class Param{
        int param1;
        String param2;

        public int getParam1() {
            return param1;
        }

        public void setParam1(int param1) {
            this.param1 = param1;
        }

        public String getParam2() {
            return param2;
        }

        public void setParam2(String param2) {
            this.param2 = param2;
        }
    }

    public static class Result<T> {
        int type;
        String res;
        T data;

        public Result(){}

        public Result(String res){ this.res = res;}

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getRes() {
            return res;
        }

        public void setRes(String res) {
            this.res = res;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}