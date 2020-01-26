
# consumer

远程调用客户端，基于openfeign，实现基于客户端的负载。
所有服务端地址从nacos中拉取。

demo中包含了get请求、post请求、post请求带文件三类接口的远端调用。
使用feign config完成对feign的相关设置。


测试：访问
http://127.0.0.1:8082/
```json
{
    "type": 200,
    "res": "Hello Nacos Discovery 1580029194012 header[dsadsada]",
    "data": null
}
```
