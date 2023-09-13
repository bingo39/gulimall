linux常用指令
=======

## 常用指令

+ 创建目录 `mkdir -p/xxxx/xxx`
+ 对目录下所有文件开放可读可写权限:
  `chmod -r 777`

### 文件/目录

+ echo指令
    + echo.....>>

> 搭配输出重定向符一起使用，将字符串内容直接写入文件中

例如：

```shell
echo "Hello World" > Doc.txt
## 在Doc.txt 文件中输入 Hello World
```

+ 设置权限 chmod

+ cat指令
    + 基本用法：cat 文件名

> 查看指定文件内容

+ 查找目录/文件 find
    + find xxx :该目录下的目录/文件
    + find / -name xxxx : 所有目录中查找xxx
    + pwd: 查看所在目录的绝对路径

## docker相关

+ docker run --name "xxxxx"

> 运行指定docker容器，没有将创建一个

```shell
docker run --name elasticsearch -p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms64m -Xmx512m" \  
-v /mydata/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /mydata/elasticsearch/data:/usr/share/elasticsearch/data \
-v /mydata/elasticsearch/plugins:/usr/share/elasticsearch/plugins \  
-d elasticsearch:7.4.2
```  

<font color="red">说明</font>

```text
-p : 暴露两个端口  
创建/运行容器 'elasticsearch' 接受http之类的请求为9200，分布式集群间发送/接受请求的为9300。  
-e "discovery.type=single-node" \ :指定运行模式为单节点
-e ES_JAVA_OPTS="-Xms64m -Xmx512m" \ : 运行起来的内存为64m,最大内存512m
-v 挂载,usr是elasticsearch的目录，mydata是外部目录
-d 使用镜像为elasticsearch:7.4.2

```


