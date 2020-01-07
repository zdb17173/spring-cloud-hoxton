
# provider

远程调用服务端，对外提供三个测试接口，分别为get、post、file三类，以测试日常接口情况。
程序启动后，会将程序注册至nacos，注册完成后客户端方可发现服务并远程调用。

服务端需要配置较少，pom加入相关依赖，application.properties中加入对应nacos地址，加入应用名即可。



