
# nacos 配置服务

配置服务结合nacos配置管理，可从nacos动态拉取配置至本地服务器。

[nacos官网](https://nacos.io/)

# 本地部署

在0.7版本之前，在单机模式时nacos使用嵌入式数据库实现数据的存储，不方便观察数据存储的基本情况。0.7版本增加了支持mysql数据源能力，具体的操作步骤：

- 1.安装数据库，版本要求：5.6.5+
- 2.初始化mysql数据库，数据库初始化文件：nacos-mysql.sql
- 3.修改conf/application.properties文件，增加支持mysql数据源配置（目前只支持mysql），添加mysql数据源的url、用户名和密码。

```properties
spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://11.162.196.16:3306/nacos_devtest?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=nacos_devtest
db.password=youdontknow
```

单机模式启动
nacos-server-1.1.4\nacos\bin

`sh startup.sh -m standalone`

# nacos控制台

http://127.0.0.1:8848/nacos

默认用户nacos 密码nacos



# nacos上传配置

创建一个配置：

<table>
    <tr>
        <td>Data Id</td>
        <td>Group</td>
        <td>content</td>
    </tr>
    <tr>
        <td>example.properties</td>
        <td>DEFAULT_GROUP</td>
        <td>
            useLocalCache=true<br>
            useLocalCache1=true<br>
            useLocalCache2=true<br>
        </td>
    </tr>
</table>

以上配置对应了spring中的bootstrap.properties中的
```properties
spring.application.name=example
spring.cloud.nacos.config.file-extension=properties
```

spring-boot中会自动拉取useLocalCache
```properties
@RestController
@RequestMapping("/config")
@RefreshScope
public class ConfigController {

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @RequestMapping("/get")
    public boolean get() {
        return useLocalCache;
    }
}
```



# demo说明

nacos配置需写在bootstrap.properties中
访问 http://127.0.0.1:8080/config/get 返回true，该配置从远端服务拉取




