
# gateway

网关服务，可通过手动配置 + (Eureka)自动发现路由服务 + 动态配置(db获取路由规则)方式，实现前端到后端微服务的调用。


## Eureka自动发现路由服务

通过discovery的配置，gateway可自动从Eureka中发现所有注册的微服务，并通过接口调用。

例如`http://127.0.0.1:9999/ribbon-service/api/test/checkLogin`，其中`ribbon-service`是微服务的serverId，对应`spring.application.name`

```yaml

spring:
  profiles: discovery
  application:
    name: "gateway"
  cloud:
    gateway:
      discovery:
        locator:
          #enable=true后可通过 http://127.0.0.1:9999/ribbon-service/api/test/checkLogin 访问
          #所有注册的服务都可以通过ServiceId前缀进行匹配访问
          enabled: true
          lowerCaseServiceId: true

```


## 手动配置

手动配置方式如下：
- 需要路由到微服务，需用`lb://{service-id}`方式
- 匹配规则predicates 例如`- Path=/t/**`
- 过滤器filters 例如StripPrefix=2 例如用/test/aaa/get/config请求 实际路由时候回用 /get/config请求
- 路由规则有很多，根据实际情况使用
```yaml
spring: static
  application:
    name: "gateway"
  cloud:
    gateway:
      routes:
      - id: t
        uri: lb://RIBBON-SERVICE
        predicates:
          - Path=/t/**
        filters:
          - StripPrefix=1
      - id: test
        uri: http://127.0.0.1:8080/
        predicates:
          - Path=/test/**
        filters:
        #剥离path前{2}个
        # 例如用/test/aaa/get/config请求 实际路由时候回用 /get/config请求
          - StripPrefix=2
        # 匹配指定路径的路由
      - id: path_route
        uri: https://h5.baike.qq.com/
        predicates:
        - Method=GET,POST
        filters:
        - StripPrefix=1
      - id: setstatusint_route
        uri: https://example.org
        predicates:
        - Path=/444/**
        filters:
        - SetStatus=401
```


## 动态配置
参考以下代码，可动态添加路由
```java
class DynamicAddRouteController{
    @Resource
    MysqlRouteDefinitionRepository mysqlRouteDefinitionRepository;

    @RequestMapping("/addRoute")
    public Result addRoute() {
        try {
            //动态注册sina到gateway
            mysqlRouteDefinitionRepository.addRouteDefinition(
                    "sina",
                    "https://www.sina.com.cn/",
                    new String[]{"Path=/sina/**"},
                    new String[]{"StripPrefix=1"}
            );

            //刷新所有路由表
            routeRefreshListener.onApplicationEvent(new RefreshScopeRefreshedEvent());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new Result(500, e.getMessage());
        }
    }
}

```


## 路由过滤器
参考AuthFilter和CorsFilter
```java

public class AuthFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        //...
    }
}

```