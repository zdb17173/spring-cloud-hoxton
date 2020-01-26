
# provider

远程调用服务端，对外提供三个测试接口，分别为get、post、file三类，以测试日常接口情况。
程序启动后，会将程序注册至nacos，注册完成后客户端方可发现服务并远程调用。

服务端需要配置较少，pom加入相关依赖，application.properties中加入对应nacos地址，加入应用名即可。



测试 访问
http://127.0.0.1:8081/echo/sdasad
```json
{
"type": 0,
"res": "Hello Nacos Discovery sdasad header[null]",
"data": null
}
```


http://127.0.0.1:8081/actuator/nacos-discovery 可查看actuator监看信息
```json
{
"subscribe": [],
"NacosDiscoveryProperties": {
"serverAddr": "127.0.0.1:8848",
"endpoint": "",
"namespace": "",
"watchDelay": 30000,
"logName": "",
"service": "nacos-producer",
"weight": 1,
"clusterName": "DEFAULT",
"group": "DEFAULT_GROUP",
"namingLoadCacheAtStart": "false",
"metadata": {
"preserved.register.source": "SPRING_CLOUD"
},
"registerEnabled": true,
"ip": "192.168.17.181",
"networkInterface": "",
"port": 8081,
"secure": false,
"accessKey": "",
"secretKey": "",
"heartBeatInterval": null,
"heartBeatTimeout": null,
"ipDeleteTimeout": null
}
}
```