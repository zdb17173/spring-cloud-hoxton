
# Ansible

ansible 的特点就在于它的简洁。让 ansible 在主流的配置管理系统中与众不同的一点便是，它并不需要你在想要配置的每个节点上安装自己的组件。同时提供的一个优点在于，如果需要的话，你可以在不止一个地方控制你的整个基础架构。

# 安装

```
yum install ansible
```

# 配置

ansible 的配置文件为/etc/ansible/ansible.cfg，ansible 有许多参数，下面我们列出一些常见的参数
- inventory = /etc/ansible/hosts #这个参数表示资源清单inventory文件的位置
- library = /usr/share/ansible #指向存放Ansible模块的目录，支持多个目录方式，只要用冒号（：）隔开就可以
- forks = 5 #并发连接数，默认为5
- sudo_user = root #设置默认执行命令的用户
- remote_port = 22 #指定连接被管节点的管理端口，默认为22端口，建议修改，能够更加安全
- host_key_checking = False #设置是否检查SSH主机的密钥，值为True/False。关闭后第一次连接不会提示配置实例
- timeout = 60 #设置SSH连接的超时时间，单位为秒
- log_path = /var/log/ansible.log #指定一个存储ansible日志的文件（默认不记录日志）

配置主机清单，修改/etc/ansible/hosts文件
```
[mysql_test]
192.168.253.159
192.168.253.160
192.168.253.153
```

# 测试

ping
```
[root@cpe-172-100-0-4 ansible]# ansible web -m ping
139.217.118.19 | SUCCESS => {
    "changed": false, 
    "ping": "pong"
}

```

查看当前目录，ansible默认使用root账号 默认路径在/root下
```
[root@cpe-172-100-0-4 azure-user]# ansible web -m shell -a 'ls -a'
139.217.118.19 | SUCCESS | rc=0 >>
.
..
anaconda-ks.cfg
.ansible
.bash_history
.bash_logout
.bash_profile
.bashrc
.cache
.config
.cshrc
id_rsa.pub
original-ks.cfg
.pki
springtest
.ssh
.tcshrc
.viminfo
xiuno-docker
```


# 远程执行脚本

编辑脚本
```
[root@cpe-172-100-0-4 azure-user]# cat a.sh
#!/bin/bash

if [! -d "fran"];then 
  mkdir fran
fi

cd fran
date >> aa.log
df -lh >> bb.log

```
执行脚本`ansible web -m script -a 'a.sh'`


# playbook
ansible执行计划可以使用完成类似docker compose的功能，完成软件安装的一系列复杂组合命令。

以下脚本实现将本地文件aa.log搬运至web组下所有服务器。并且在web组下所有服务器执行docker命令，启动spring-1.0。
```
[root@cpe-172-100-0-4 ansible-example]# cat site.yml 
---
- hosts: web
  tasks:
    - name: ping
      template:
        src: "/root/ansible-example/templates/aa.log"
        dest: "/root/fran/aa.log"
    - name: startspring-1.0
      command: docker start spring-1.0

```

playbook执行命令
```
[root@cpe-172-100-0-4 ansible-example]# ansible-playbook site.yml

PLAY [web] *******************************************************************************************************************************************************************

TASK [Gathering Facts] *******************************************************************************************************************************************************
ok: [139.217.118.19]

TASK [ping] ******************************************************************************************************************************************************************
changed: [139.217.118.19]

PLAY RECAP *******************************************************************************************************************************************************************
139.217.118.19             : ok=2    changed=1    unreachable=0    failed=0   

```


# playbook特殊脚本

### Handlers
Handlers也是一些task的列表，和一般的task并没有什么区别。Handlers是由通知者进行的notify，如果没有被notify，则Handlers不会执行，假如被notify了，则Handlers被执行
不管有多少个通知者进行了notify，等到play中的所有task执行完成之后，handlers也只会被执行一次。

```
---
- hosts: web
  vars:
    - package: "franfranfran"
  tasks:
    - name: ping
      template:
        src: "/root/ansible-example/templates/aa.log"
        dest: "/root/fran/aa.log"
    - name: startspring-1.0
      command: docker start spring-1.0
      tags:
        - start
      notify:
        - copydd
    - name: copy file
      copy: content="{{package}}" dest="/root/fran/cc.log"

  handlers:
    - name: copydd
      copy: content="{{package}}" dest="/root/fran/dd.log"

```
以上命令，只有执行到startspring-1.0任务的notify时才会执行handlers copydd


### 变量

变量有两种方式，一种是直接写在yml脚本中，另一种是写在/etc/ansible/hosts中
```
# This is the default ansible 'hosts' file.
#
# It should live in /etc/ansible/hosts
#
#   - Comments begin with the '#' character
#   - Blank lines are ignored
#   - Groups of hosts are delimited by [header] elements
#   - You can enter hostnames or ip addresses
#   - A hostname/ip can be a member of multiple groups

[web]
139.217.118.19 id="1"
```
或者
```
[root@cpe-172-100-0-4 ansible-example]# cat site.yml 
---
- hosts: web
  vars:
    - package: "franfranfran"
```

变量使用可以在脚本中，也可以在template中，引用方式均为{{变量名}}
```
---
- hosts: web
  vars:
    - package: "franfranfran"
  tasks:
    - name: copy file
      copy: content="{{package}}" dest="/root/fran/aa.log"
  
```

### 条件判断
when的值是一个条件表达式，如果条件判断成立，这个task就执行，如果判断不成立，则task不执行。

```
---
- hosts: web
  vars:
    - package: "franfranfran"
  tasks:
    - name: copy file1
      copy: content="{{package}}" dest="/root/fran/aa.log"
      when: ansible_distribution == "CentOS"
    - name: copy file2
      copy: content="{{package}}" dest="/root/fran/aa.log"
      when: 
        - ansible_distribution == "CentOS"
        - ansible_distribution_major_version == 7
```
以上是单条件判断和联合条件判断的写法


### templates
template类是Jinja的另一个重要组件，可以看作一个编译过的模块文件，用来生产目标文本，传递Python的变量给模板去替换模板中的标记。ansible会自动将templates的文件中的{{variablename}}进行替换。
```
[root@cpe-172-100-0-4 ansible-example]# cat site.yml 
---
- hosts: web
  vars:
    - package: "franfranfran"
  tasks:
    - name: ping
      template: 
        src: "/root/ansible-example/templates/aa.log" 
        dest: "/root/fran/aa.log"
    - name: startspring-1.0
      command: docker start spring-1.0
      tags:
        - start 

#执行上述脚本时，src的aa.log中的{{package}}会被替换为franfranfran
```


### tags
tags，在一个playbook中，我们一般会定义很多个task，如果我们只想执行其中的某一个task或多个task时就可以使用tags标签功能了。事实上，不光可以为单个或多个task指定同一个tags。playbook还提供了一个特殊的tags为always。作用就是当使用always当tags的task时，无论执行哪一个tags时，定义有always的tags都会执行。
```
---
- hosts: web
  tasks:
    - name: ping
      template: 
        src: "/root/ansible-example/templates/aa.log" 
        dest: "/root/fran/aa.log"
      tags:
        - always
    - name: startspring-1.0
      command: docker start spring-1.0
      tags:
        - start 

ansible-playbook site.yml --tags=""
```




