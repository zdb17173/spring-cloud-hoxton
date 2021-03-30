
# spring-cloud-hoxton版本demo

[官方文档](https://https://docs.spring.io/spring-cloud/docs/Hoxton.SR10/reference/html/)

spring cloud提供了大量的工具和模型以支持分布式系统，以实现配置管理、服务发现、
熔断保护、智能路由、代理等。

Release Train Version: Hoxton.10

Supported Boot Version: 2.3.8.RELEASE

通过以下spring cloud组件，可以完成上述的各种功能。

1. Spring Cloud Config
2. Spring Cloud Netflix
3. Spring Cloud OpenFeign
4. Spring Cloud Bus
5. Spring Cloud Sleuth
6. Spring Cloud Consul
7. Spring Cloud Zookeeper
8. Spring Boot Cloud CLI
9. Spring Cloud Security
10. Spring Cloud for Cloud Foundry
11. Spring Cloud Contract Reference Documentation
12. Spring Cloud Vault
13. Spring Cloud Gateway
14. Spring Cloud Function
15. Spring Cloud Kubernetes
16. Spring Cloud GCP
17. Spring Cloud Circuit Breaker
18. Spring Cloud Stream

demo中使用了
1. nacos 服务发现、配置管理，详细见 [nacos配置](/nacos-spring-cloud-config-example/readme.md). 
2. openfeign 服务间的客户端端负载均衡
3. spring cloud config 配置管理，详细见[config服务配置](/config-server/readme.md).


运维
1. [ansible](/ansible.md)来实现多服务器的部署、启动、停止。



