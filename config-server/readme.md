
# 官方文档
https://docs.spring.io/spring-cloud-config/docs/2.2.7.RELEASE/reference/html/




# config-server

## File System Backend
启动程序后，如使用本地模式，会拉取repo文件夹下的配置文件
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

local文件命名规则
```yaml
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```

## JDBC Backend

初始化表结构
```sql
drop table if exists config_application;

drop table if exists config_property;

/*==============================================================*/
/* Table: config_application                                    */
/*==============================================================*/
create table config_application
(
   id                   bigint not null auto_increment,
   app_name             varchar(255),
   profile              varchar(255),
   label                varchar(255),
   remark               varchar(255),
   create_time          datetime,
   update_time          datetime,
   primary key (id)
);
ALTER TABLE `config_application` ADD UNIQUE INDEX `UNIQUE_KEY` (`app_name`, `profile`, `label`); 

/*==============================================================*/
/* Table: config_property                                       */
/*==============================================================*/
create table config_property
(
   id                   bigint not null auto_increment,
   app_id               bigint not null,
   property_key         varchar(255),
   property_value       varchar(255),
   remark               varchar(255),
   create_time          datetime,
   update_time          datetime,
   primary key (id)
);


```

初始化配置数据
```sql
insert into `config_application` (`id`, `app_name`, `PROFILE`, `label`, `remark`, `create_time`, `update_time`) values('1','db','dev','master',NULL,'2021-03-24 11:32:55','2021-03-24 11:32:57');
insert into `config_application` (`id`, `app_name`, `PROFILE`, `label`, `remark`, `create_time`, `update_time`) values('2','swagger','dev','master',NULL,'2021-03-24 11:33:11','2021-03-24 11:33:13');
insert into `config_application` (`id`, `app_name`, `PROFILE`, `label`, `remark`, `create_time`, `update_time`) values('3','security','dev','master',NULL,'2021-03-24 11:33:41','2021-03-24 11:33:43');


insert into `config_property` (`id`, `app_id`, `property_key`, `property_value`, `remark`, `create_time`, `update_time`) values('2','1','spring.datasource.url','jdbc:mysql://localhost:3306/cas?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false&serverTimezone=Asia/Shanghai',NULL,NULL,NULL);
insert into `config_property` (`id`, `app_id`, `property_key`, `property_value`, `remark`, `create_time`, `update_time`) values('3','1','spring.datasource.username','root',NULL,NULL,NULL);
insert into `config_property` (`id`, `app_id`, `property_key`, `property_value`, `remark`, `create_time`, `update_time`) values('4','1','spring.datasource.password','123456',NULL,NULL,NULL);
insert into `config_property` (`id`, `app_id`, `property_key`, `property_value`, `remark`, `create_time`, `update_time`) values('5','1','spring.datasource.hikari.maximum-pool-size','20',NULL,NULL,NULL);
insert into `config_property` (`id`, `app_id`, `property_key`, `property_value`, `remark`, `create_time`, `update_time`) values('6','1','spring.datasource.hikari.max-lifetime','30000',NULL,NULL,NULL);
insert into `config_property` (`id`, `app_id`, `property_key`, `property_value`, `remark`, `create_time`, `update_time`) values('7','1','spring.datasource.hikari.hikari.hikari.idle-timeout','30000',NULL,NULL,NULL);
insert into `config_property` (`id`, `app_id`, `property_key`, `property_value`, `remark`, `create_time`, `update_time`) values('8','1','spring.datasource.hikari.hikari.data-source-properties.prepStmtCacheSize','250',NULL,NULL,NULL);
insert into `config_property` (`id`, `app_id`, `property_key`, `property_value`, `remark`, `create_time`, `update_time`) values('9','1','spring.datasource.hikari.hikari.data-source-properties.prepStmtCacheSqlLimit','2048',NULL,NULL,NULL);
insert into `config_property` (`id`, `app_id`, `property_key`, `property_value`, `remark`, `create_time`, `update_time`) values('10','1','spring.datasource.hikari.hikari.data-source-properties.cachePrepStmts','true',NULL,NULL,NULL);
insert into `config_property` (`id`, `app_id`, `property_key`, `property_value`, `remark`, `create_time`, `update_time`) values('11','1','spring.datasource.hikari.hikari.data-source-properties.useServerPrepStmts','true',NULL,NULL,NULL);
```

初始化应用测试数据
```sql
create table `auth_user` (
	`id` bigint (20),
	`name` varchar (576),
	`account` varchar (2295),
	`foreign_name` varchar (576),
	`email` varchar (576),
	`phone` varchar (576),
	`password` varchar (576),
	`status` int (11),
	`create_time` datetime 
); 
insert into `auth_user` (`id`, `name`, `account`, `foreign_name`, `email`, `phone`, `password`, `status`, `create_time`) values('1','ed user','ed','ed',NULL,NULL,'1','0','2018-04-13 15:56:04');
insert into `auth_user` (`id`, `name`, `account`, `foreign_name`, `email`, `phone`, `password`, `status`, `create_time`) values('2','ced user','ced','ced',NULL,NULL,'1','0','2018-04-13 15:56:32');
insert into `auth_user` (`id`, `name`, `account`, `foreign_name`, `email`, `phone`, `password`, `status`, `create_time`) values('3','fed user','fed','fed',NULL,NULL,'1','0','2018-04-13 15:56:47');
insert into `auth_user` (`id`, `name`, `account`, `foreign_name`, `email`, `phone`, `password`, `status`, `create_time`) values('4','ed2 user','ed2','ed2',NULL,NULL,'1','0','2018-04-13 15:59:46');
insert into `auth_user` (`id`, `name`, `account`, `foreign_name`, `email`, `phone`, `password`, `status`, `create_time`) values('5','ar_ed user','ar_ed','ar_ed',NULL,NULL,'1','0','2018-04-13 16:00:05');
insert into `auth_user` (`id`, `name`, `account`, `foreign_name`, `email`, `phone`, `password`, `status`, `create_time`) values('16','ar_ced user','ar_ced','ar_ced',NULL,NULL,'1','0','2018-04-13 16:00:36');
insert into `auth_user` (`id`, `name`, `account`, `foreign_name`, `email`, `phone`, `password`, `status`, `create_time`) values('18','ar_ed1 user','ar_ed1','ar_ed1',NULL,NULL,'1','0','2018-04-13 16:00:56');
insert into `auth_user` (`id`, `name`, `account`, `foreign_name`, `email`, `phone`, `password`, `status`, `create_time`) values('19','sp_live_ed user','sp_live_ed','sp_live_ed',NULL,NULL,'1','0','2018-04-13 16:01:27');

```

启动服务后访问以下地址，可获得
http://127.0.0.1:7100/db/dev
```json
{
    "name": "db",
    "profiles": [
        "dev"
    ],
    "label": null,
    "version": null,
    "state": null,
    "propertySources": [
        {
        "name": "db-dev",
        "source": {
            "spring.datasource.type": "com.zaxxer.hikari.HikariDataSource",
            "spring.datasource.url": "jdbc:mysql://localhost:3306/cas?useUnicode=true&characterEncoding=utf8mb4&autoReconnect=true&useSSL=false",
            "spring.datasource.username": "root",
            "spring.datasource.password": "123456",
            "spring.datasource.hikari.maximum-pool-size": "20",
            "spring.datasource.hikari.max-lifetime": "30000",
            "spring.datasource.hikari.hikari.hikari.idle-timeout": "30000",
            "spring.datasource.hikari.hikari.data-source-properties.prepStmtCacheSize": "250",
            "spring.datasource.hikari.hikari.data-source-properties.prepStmtCacheSqlLimit": "2048",
            "spring.datasource.hikari.hikari.data-source-properties.cachePrepStmts": "true",
            "spring.datasource.hikari.hikari.data-source-properties.useServerPrepStmts": "true"
            }
        }
    ]
}
```
## 通过单元测试同步配置到数据库

运行YamlImportTest.java，可将test/resources/repo目录下的所有配置文件扫描
同步到mysql db中。

文件命名中包含了appname、profile、label三部分，三部分以`-`做为分隔
- application不可为空
- 默认profile为profile  
- 默认label为master  

例如：
```yaml
appname-profile-label.yaml   
appname-profile.yaml  
appname.yaml
```



文件格式支持：  
- yaml 
- yml 
- properties



# config-client

注意事项：
- config相关配置需要放到bootstrap.yml中。
- 
