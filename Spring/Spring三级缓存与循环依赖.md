在完全掌握Spring IoC原理的基础上，理解Spring内部对依赖注入的处理原理

## 什么是循环依赖？

如下图：

![image-20210507144637643](process\image-20210507144637643.png)

BeanA类依赖了BeanB类，同时BeanB类又依赖BeanA类。这种依赖关系形成了一个闭环，我们把这种依赖关系叫做循环依赖。同理，再如下图的情况：

![image-20210507144808415](process\image-20210507144808415.png)

上图中，BeanA类依赖了BeanB类，BeanB类依赖了BeanC类，BeanC类依赖了BeanA类，如此也形成了一个依赖闭环。 再比如：

![image-20210507144916908](process\image-20210507144916908.png)

上图中，自己引用了自己，自己与自己形成了依赖关系。同样也是一个依赖闭环。

## 循环依赖问题复现

### 定义依赖关系

我们继续扩展前面章节的内容， 给ModifyService增加一个属性，代码如下：

```java
@GPService
public class ModifyService implements IModifyService{
    @GPAutowired
    private QueryService queryService;
    ....
}
```

给QueryService增加一个属性，代码如下：

```java
@GPService
@Slf4j
public class QueryService implements IQueryService{
    @GPAutowired
    private ModifyService modifyService;
    ...
}
```

如此，就出现了循环依赖，依赖闭环。

![image-20210507145453318](process\image-20210507145453318.png)

启动项目，我们发现只要是有循环依赖关系的属性并没有自动赋值，而没有循环依赖关系的属性均有自动赋值，如下图所示：

![image-20210507145604695](process\image-20210507145604695.png)

这种情况是怎么造成的呢？我们分析原因之后发现，因为IoC容器对Bean的初始化是根据BeanDefinition循环迭代，有一定的顺序。这样，在执行依赖注入时，需要自动赋值的属性对应的对象有可能还没初始化，没有初始化也就没有对应的实例可以注入。

## 使用缓存解决循环依赖问题

1、通过构造器注入的不能支持循环依赖

2、非单例的，不支持循环依赖

一级缓存：所有的成熟Bean

二级缓存：原生的早期Bean

三级缓存：代理的Bean

![image-20210507150135621](process\image-20210507150135621.png)

### 定义缓存

具体代码如下：

```java
//循环依赖的标识---当前正在创建的实例bean
private Set<String> singletonsCurrectlyInCreation =
    new HashSet<String>();
//一级缓存
private Map<String,Object> singletonObjects = 
    new HashMap<String , Object>();
//二级缓存
private Map<String,Object> earlySingletonObjects = 
    new HashMap<String, Object>();
```

## 判断循环依赖

增加getSingleton()方法：

```java
/**
*判断是否是循环引用的出口。
*@Param beanName
*@return
*/
private Object getSingleton(String beanName , GPBeanDefinition beanDefinition){
    //先去一级缓存里拿,
    Object bean = singletonObjects.get(beanName);
    //一级缓存中没有，但是正在创建的bean标识中有，说明是循环依赖
    if(bean ==null && singletonsCurrentlyInCreation.contains(beanName)){
        bean = earlySingletonObjects.get(beanName);
        //如果二级缓存中没有， 就从三级缓存中拿
        if(bean == null ){
            //从三级缓存中取
            Object object = instantinateBean(beanName , beanDefinition);
            //然后将其放入到二级缓存中，因为如果有多次依赖，就去二级缓存中判断，已经有了就不在再次创建了
            earlySingletonObjects.put(beanName , object);
        }
    }
    return bean;
}
```

## 添加缓存

修改getBean() 方法，在getBean（）方法中添加如下代码：

```java
//Bean 的实例化，DI是从而这个方法开始的
public Object getBean(String beanName){
    //1、先拿到BeanDefinition配置信息
    GPBeanDefinition beanDefinition = 
        regitry.beanDefinitionMap.get(beanName);
    //增加一个出口。判断实体类是否已经被加载过了
    Object singleton = getSingleton(beanName , beanDefinition);
    if(singleton !=null){return singleton;}
    //标记bean正在创建
    if(!singletonsCurrentlyInCreation.contains(beanName)){
        singletonsCurrentlyInCreation.add(beanName);
    }
    //2、反射实例化newInstance();
    Object instance = instantiateBean(beanName , beanDefinition);
    
    //放入一级缓存
    this.singletonObjects.put(beanName , instance);
    
    //3、封装成一个叫做BeanWrapper
    GPBeanWrapper beanWrapper = new GPBeanWrapper(instance );
    
    //4、执行依赖注入
    populateBean(beanName , beanDefinition,beanWrapper);
    //5、保存到IoC容器
    factoryBeanInstanceCache.put(beanName , beanWrapper);
    
    return beanWrapper.getWrapperInstance();
}
```

添加依赖注入

修改populateBean()方法，代码如下

```java
private void populateBean(String beanName , GPBeanDefinition beanDefinition , GPBeanWrapper beanWrapper){
    ...
        try{
         //ioc.get(beanName);相当于通过接口的全名拿到接口的实现的实例
            field.set(instance , getBean(autowiredBeanName));
        }catch(IllegalAccessException e){... ; continue;}
}
```

