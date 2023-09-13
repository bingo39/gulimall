# Object划分

1. PO（Persistant Object）持久对象

> PO 就是对应数据库中某个表中的一条记录，多个记录可以用PO的集合。PO中不应该不包含任何对数据库的操作

2. DO (Domain Object) 领域对象

> 就是从现实世界中抽象出来的有形或无形的业务实体

3. TO (Transfer Object) 数据传输对象

> 不同的应用程序之间传输的对象

4. DTO (Data Transfer Object) 数据传输对象

> 源于J2ee的设计模式，原来目的是为了EJB的分布式应用提供粗粒度的数据实体，以减少分布式调用的次数，从而提高分布式的性能和降低网络负载;  
> 这里,泛指用于展示层与服务层之间的数据传输对象。

5. VO (Value Object) 值对象

> 通常用于业务层之间的数据传递，和PO一样也是仅仅包含数据而已。但应是抽象出的业务对象，可以和表对应，也可以不，这根据业务的需要。  
> 用new关键字创建，由GC回收  
> 也可以叫   <font color=red>View Object : 视图对象</font>  
① 接受页面传递来的数据，封装对象  
② 将业务处理完成的对象，分装成页面要用的数据

+ 举例说明：
  > 平时在Entity包中放入太多响应注解（eg：@TableField,@JsonInclude(JsonInclude.xxx)），很乱很不规范。

6. BO (Business Object)业务对象

> 从业务模型的角度看，见UML元件领域模型中的领域对象。封装业务逻辑的java对象，通过调用DAO方法，结合PO,VO进行业务操作。</br>
> business object:业务对象，主要作用是把业务逻辑封装为一个对象。这个对象可以包括一个或多个其他的对象。

+ 举例说明：
  > 比如一个简历，有教育经历、工作经历、社会关系等等。我们可以把教育经历对应一个PO，工作经历对应一个PO，社会关系对应一个PO。建立一个对简历的BO对象处理简历，每个BO包含这些PO。这样处理业务逻辑时，就可以针对BO去处理。

7. POJO (Plain Ordinary Object) 简单无规则java对象

> 最基本的传统的java bean ,只有属性字段以及setter 和 getter方法。  
> POJO是DO/DTO/BO/VO的统称

8. DAO (Data Access Object) 数据访问对象

> 是一个sun的一个标准j2ee设计模式，这个模式中有个接口就是DAO，它负责持久层的操作。为业务层提供接口。此对象用于访问数据库。通常和PO结合使用，DAO 中包含了各种数据库的操作方法。  
> 通过它的方法，结合PO对数据库进行相关的操作。夹在业务逻辑与数据资源中间。配合VO提供数据库的CRUD操作。
