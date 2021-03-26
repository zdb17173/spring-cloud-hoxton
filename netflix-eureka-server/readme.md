
### spring cloud集群配置

1、启动eureka server

运行EurekaApplication.java，启动成功后可访问http://localhost:8761/ 进入eureka管理后台

2、启动eureka provider1和eureka provider2

运行Provider1Application.java和Provider2Application.java启动服务提供端
- 访问http://127.0.0.1:8001/swagger-ui.html 查看服务提供方的相关api

3、启动消费端

运行FeignClientApplication.java启动基于Feign的服务消费端
- 访问http://127.0.0.1:8081/ 测试调用

运行ClientApplication启动基于RestTemplate的服务消费端
- 访问http://127.0.0.1:8081/user/1 测试调用


