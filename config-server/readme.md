
# config-server

## 启动程序后，如使用本地模式，会拉取repo文件夹下的配置文件
```
http://127.0.0.1:7100/app/* 返回app配置
http://127.0.0.1:7100/app1/test 返回app1-test.yml
http://127.0.0.1:7100/app1/prod 返回app1-prod.yml

返回
{
    "name": "app1",
    "profiles": [
    "prod"
    ],
    "label": null,
    "version": null,
    "state": null,
    "propertySources": [
        {
        "name": "classpath:/repo/app1-prod.yml",
        "source": {
            "db.url": "prod",
            "db.age": 1,
            "user.name": "zdb-prod"
            }
        }
    ]
}
```

## 命名规则
```yaml
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```



# config-client

注意事项：
- config相关配置需要放到bootstrap.yml中。
- 
