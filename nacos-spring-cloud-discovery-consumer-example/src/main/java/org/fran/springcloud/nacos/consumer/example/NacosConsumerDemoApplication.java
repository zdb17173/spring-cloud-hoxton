package org.fran.springcloud.nacos.consumer.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Spencer Gibb
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableFeignClients
public class NacosConsumerDemoApplication {
	@Autowired
	HelloClient helloClient;

	@RequestMapping("/")
	public Result hello() {
		Result r = helloClient.echo(System.currentTimeMillis() + "");
		r.setType(200);

		Param p = new Param();
		p.setParam1(111);
		p.setParam2("paramString");
		Result<String> r2 = helloClient.post(p);

		Result r3 = helloClient.upload(FileToMultipartFile.getMulFile(
				new File("C:\\temp\\test.jpg")),
				"description",
				"name"
		);

		return r;
	}

	public static void main(String[] args) {
		SpringApplication.run(NacosConsumerDemoApplication.class, args);
	}


	// https://cloud.spring.io/spring-cloud-openfeign/reference/html/#spring-cloud-feign
	@FeignClient(name = "nacos-producer", configuration = FeignConfig.class)
	interface HelloClient {
		@GetMapping(value = "/echo/{string}", consumes = "application/json")
		public Result echo(@PathVariable("string") String string);

		@PostMapping(value = "/post", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Result<String> post(@RequestBody Param baseParam);

		@PostMapping(value = "/upload",
				consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
				produces = MediaType.APPLICATION_JSON_VALUE)
		public Result upload(
				@RequestPart("uploadFile")MultipartFile uploadFile,
				@RequestParam("description")String description,
				@RequestParam("name")String name);
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