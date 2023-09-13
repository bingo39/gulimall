ElasticSearch入门
==========

## 简介

Elasticsearch是一个基于Apache Lucene(TM)的开源搜索引擎。 无论在开源还是专有领域，Lucene可以被认为是迄今为止最先进、性能最好的、功能最全的搜索引擎库。

官方文档:  https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html

## ElasticSearch基本概念

基本概念
![ElasticSearch基本概念](../image/ElasticSearch%E6%A6%82%E5%BF%B5.jpg)

1. Index（索引）</br>
   动词，相当于 MySQL 中的 insert;  
   名词，相当于 MySQL 中的 Database

2. Type（类型)  
   在 Index（索引）中，可以定义一个或多个类型

3. Document(文档)  
   文档是 JSON 格式，保存在某个索引（Index）下某种类型（Type）的一个数据（Document）  
   Document就像是MySQL中的某个Table里面的内容:

4. 倒排索引机制

## ElasticSearch 指令

### _cat检索

(可使用postman模拟请求)

```text
GET /_cat/nodes：查看所有节点
GET /_cat/health：查看 es 健康状况
GET /_cat/master：查看主节点
GET /_cat/indices：查看所有索引 show databases;
```

### QueryDSL语法入门

+ 迁移数据库

```dsl
POST _reindex
{
  "source": {
    "index": "product" 
  },
  "dest": {
    "index": "gulimall_product"
  }
}
```

将product文档中的数据迁移到gulimall_product

## ik分词器

### 自定义词库

思路:安装nginx里安装自定义词库，来处理自定义词语





