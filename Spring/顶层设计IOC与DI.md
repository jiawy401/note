## 从Servlet到ApplicationContext

我们已经了解SpringMVC的入口是DispatcherServlet , 我们实现了DispatcherServlet的init()方法。在init()方法中完成了IOC容器的初始化。而在我们使用Spring的经验中，我们见得最多的是ApplicationContext，似乎Spring托管的所有实例Bean都可以通过调用getBean()方法来获得。那么ApplicationContext又是从何而来的？从Spring源码中我们可以看到，DispatcherServlet的类图如下：

![image-20210426135141350](process\image-20210426135141350.png)

DispatcherServlet继承了FrameworkServlet,FrameworkServlet继承了HttpServletBean , 

HttpServletBean继承了HttpServlet。在HttpServletBean 的init()方法中调用了FrameworkServlet的initServletBean()方法，在initServletBean()方法中初始化WebApplicationContext实例。在initServletBean()方法中调用了DispatcherServlet重写的onRefresh()方法。在DispatcherServlet的onRefresh()方法中又调用了initStrategies()方法，初始化SpringMVC的九大组件。

​	其实，上面复杂的调用关系，我们可以简单的得出一个结论：就是在Servlet的init()方法中初始化了IOC容器和SpringMVC所依赖的九大组件。

## 项目环境搭建

### application.properties配置

还是先从application.properties文件开始，用application.properties来代替application.xml，具体配置如下：

```xml
#托管的类扫描包路径#
scanPackage=com.gupaoedu.vip.demo
```

### pom.xml配置

接下来pom.xml的配置，主要关注jar依赖：

```xml
<properties>
	<!-- dependency versions -->
    <servlet.api.version>2.4</servlet.api.version>
</properties>
<dependencies>
	<!-- requied start -->
    <dependency>
    	<groupId>javax.servlet</groupId>
        <artifactId>servlet-api</artifactId>
        <version>${servlet.api.version}</version>
        <scope>provided</scope>
    </dependency>
    	<!-- requied end -->
</dependencies>
```

### web.xml

```xml
<?xml version ="1.0" encoding="UTF-8"?>
<web-app ...>
	<display-name>Gupao Web Application</display-name>
    <servlet>
    	<servlet-name>gpmvc</servlet-name>
        <servlet-class>com...spring.framework.webmvc.servlet.GPDispatcherServlet</servlet-class>
        <init-para>
        	<param-name>contextConfigLocation</param-name>
            <param-value>application.properties</param-value>
        </init-para>
        <load-on-startup>1</load-on-startup>
        
    </servlet>
    <servlet-mapping>
    	<servlet-name>gpmvc</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
</web-app>
```

### GPDispatcherServlet实现

```java
import javax.servlet.ServletConfig;
import javax.servlet.ServletException; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GPDispatcherServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException , IOException {
        this.doPost(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req , HttpServletResponse resp) throws ServletEception , IOEXception {
        
    }
}
```

### IOC顶层结构设计

annotation(自定义配置)模块

Annotation 的代码实现我们还是沿用mini版本的不变，复制过来即可。

@GPService注解：

```java
import java.lang.annotation.*;
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPService{
    String value() default "";
}
```

@GPAutowired注解：

```java
import java.lang.annotation.*;
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPAutowired{
    String value() default "";
}
```

@GPController注解：

```java
import java.lang.annotation.* ; 
@Target({ElementType.Type})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPController{
    String value() default "";
}
```

@GPRequestMapping注解：

```java
import java.lang.annotation.*;
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPRequestMapping{
    String value() default "";
}

```

@GPRequestParam注解：

```java
import java.lang.annotation.*;
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPRequestParam{
    String value() default "";
}
```

### beans(配置封装)模块

GPBeanDefinition

```java
public class GPBeanDefinition{
    private String beanClassName;
    private String factoryBeanName;
    ....setter  ....getter 
}
```

GPBeanWrapper

```java
public class GPBeanWrapper{
    private Object wrappedInstance;
    private Class<?> wrappedClass;
    public GPBeanWrapper(Object wrappedInstance){
        this.wrappedClass = wrappedInstance.getClass();
        this.wrappedInstance = wrappedInstance;
    }
    public Object getWrappedInstance(){
        return this.wrappedInstance;
    }
    //返回代理以后的Class
    //可能会是这个$Proxy0
    public Class<?> getWrappedClass(){
        return this.wrappedClass;
    }
}
```

context(IOC容器)模块

GPApplicationContext

```java
import spring.framework.annotation.GPAutowired;
import spring.framework.annotation.GPController;
import spring.framework.annotation.GPService;
import spring.framework.beans.GPBeanWrapper;
import spring.framework.beans.config.GPBeanDefinition;
import spring.framework.beans.support.GPBeanDefinitionReader;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties; 
import java.util.concurrent.ConcurrentHashMap;

public class GPApplicationContext{
    //存储注册信息的BeanDefinition
    protected final Map<String, GPBeanDefinition> beanDefinitionMap = 
        new ConcurrentHashMap<String,GPBeanDefinition>();
    private String[] configLocation;
    private GPBeanDefinitionReader reader; 
   //单例IOC容器缓存
    private Map<String, Object> factoryBeanObjectCache = 
        new ConcurrentHashMap<String,Object>();
    //通用的IOC容器
    private Map<String , GPBeanWrapper> factoryBeanInstanceCache = 
        new ConcurrentHashMap<String, GPBeanWrapper>();
    public GPApplicationContext(String ... configLocations){
        this.configLocations = configLocatoins ; 
        try{
            //1、定位，定位配置文件
            reader = new GPBeanDefinitionReader(this.configLocations);
            
            //2、加载配置文件，扫描相关的类，把它们封装成BeanDefinition
            List<GPBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
            
            //3、注册， 把配置信息放到容器里面（伪IOC容器）
            doRegisterBeanDefinition(beanDefinitions);
            
            //4、完成自动依赖注入
            doAutowired();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    //只处理非延时加载的情况
    private void doAutowired(){
        for(Map.Entry<String,GPBeanDefinition> beanDefinitionEntry : this.beanDefinitionMap.entrySet()){
            String beanName = beanDefinitionEntry.getKey();
            try{
                getBean(beanName);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    private void doRegisterBeanDefinition(List<GPBeanDefinition> beanDefinitions) throws Exception{
        for(GPBeanDefinition beanDefinition : beanDefinitions){
            if(this.beanDefinitionMap
               .containsKey(beanDefinition.getFactoryBeanName())){
               throw new Exception ("The “"+
                                    beanDefinition.getFactoryBeanName() 
                                    + "” is exists");
            }
            this.beanDefinitionMap.put(beanDefinition.getBeanClassName() , beanDefinition);
            this.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),
                                      beanDefinition);
            
        }
        //到这里为止，容器初始化完毕
    }
    
    public Object getBean(Class<?> beanClass ) throws Exception{
        return getBean (beanClass.getName())
    }
    
    //依赖注入从这里开始
    public Object getBean(String beanName) throws Exception{
        return null;
    }
    
    public String [] getBeanDefinitionNames(){
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }
    
    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }
    public Properties getConfig(){
     	return this.reader.getConfig();   
    }
}
```

GPBeanDefinitionReader 

```java
import spring.framework.beans.config.GPBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GPBeanDefinitionReader{
    private List<String> registyBeanClasses = new ArrayList<String>();
    private Properties config = new Properties();
    
    //固定配置文件中的key，相对于xml的规范
    private final String SCAN_PACKAGE = "scanPackage";
    
    public GPBeanDefinitionReader(String... locations){
        //通过URL定位找到其所对应的文件，然后转换为文件流
        InputStream is = this.getCLass().getCLassLoader().getResourceAsStream(locations[0].replace("classpath:" , ""));
        try{
            config.load(is);
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            ...
                is.close();
            ...
        }
        
        doScanner(config.getProperty(SCAN_PACKAGE));
    }
    
    private void doScanner(String scanPackage){
        //转换为文件路径，实际上就是把.替换为/就OK了
        URL url = this.getClass().getClassLoader().getResource("/"+scanPackage.replaceAll("\\." , "/"));
        File classPath = new File(url.getFile());
        for(File file : classPath.listFiles()){
            if(file.isDirectory()){
                doScanner(scanPackage + "." + file.getName()) ;
                
            }else {
                if(!file.getName().endsWith(".class")){continue;}
                String className = (scanPackage +"." + file.getName().replace(".class",""));
                registyBeanClasses.add(className);
            }
        }
    }
    
    public Properties getConfig(){
        return this.config;
    }
    
    //把配置文件中扫描到的所有的配置信息转换为GPBeanDefinition对象，以便于之后IOC操作方便
    public List<GPBeanDefinition> loadBeanDefinition(){
        List<GPBeanDefinition> result = new ArrayList<GPBeanDefinition)();
        try{
            for(String className : registyBeanClasses){
                Class<?> beanClasses = Class.forName(className);
                //如果是一个接口，是不能实例化的
                //用它实现类来实例化
                if(beanClass.isInterface()){continue;}
                
                //beanName 有三种情况：
                //1、默认是类名首字母小写
                //2、自定义名字
                //3、接口注入
                result.add(doCreateBeanDefinition(toLowerFirstCase(beanClass.getSimpleName()) , beanClass.getName()));
                Class<?> [] interfaces = beanClassgetInterfaces();
                for(Class<?> i : interfaces){
                    //如果是多个实现类，只能覆盖
                    //为什么？因为Spring没有那么只能，就是这么傻
                    //这个时候，可以自定义名字
                    result.add(doCreateBeanDefinition(i.getName(),beanClass.getName()));
                }
            }
        }catch(Exception e){
            ...
        }
        return result;
    }
    
    //如果类名本身是小写字母，确实会出问题
    //但是我要说明的是，这个方法是我自己用，private的
    //传值也是自己传，类也都遵循了驼峰命名法
    //默认传入的值，存在首字母小写的情况，也不可能出现非字母的情况
    
    //为了简化逻辑，就不做其他判断了
    private String toLowerFirstCase(String simpleName){
        chars[0] +=32;
        return String.valeuOf(chars);
    }
}
```

### 完成DI依赖注入功能

依赖注入的入口是从getBean()方法开始的，前面的IOC手写部分基本流程已通。先在GPApplicationContext中定义好，IOC容器，一个是GPBeanwrapper， 一个是单例对象缓存

```java
public class  GPApplicationCOntext{
    //存注册信息的BeanDefinition
    protected final Map<String , GPBeanDefinition> beanDefinitionMap = 
        new ConcurrentHashMap<String , GPBeanDefinition>();
    private String [] configLocations ; 
    private GPBeanDefinitionReader reader ; 
    
    //单例的IOC容器缓存
    private Map<String , Object > factoryBeanObjectCache = 
        new ConcurrentHashMap<String , Object>();
    //通用的IOC容器
    private Map<String , GPBeanWrapper> factoryBeanInstanceCache = 
        new ConcurrentHashMap<String,GPBeanWrapper>();
    ....
}
```

#### 从getBean()开始

下面，从完善getBean()方法开始：

```java
public Object getBean(Class<?> beanClass) throws Exception{
	return getBean(beanClass.getName());
}
//依赖注入，从这里开始，通过读取BeanDefinition中的信息。
//然后，通过反射机制创建一个实例并返回
//Spring做法是 ， 不会把最原始的对象放出去，会用一个BeanWrapper来进行一次包装
//装饰器模式：
//1、保留原来的OOP关系
//2、我需要对它进行扩展，增强（为了以后AOP打基础）
public Object getBean(String beanName) throws Exception{
    //1、读取配置信息
    GPBeanDefinition gpBeanDefinition = this.beanDefinitionMap.get(beanName);
    Object instance = null;
    
    //2、实例化
    instance = instantiateBean(beanName , gpBeanDefinition);
    
    //3、把这个对象封装到BeanWrapper中
    GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);
    
    //4、把BeanWrapper 存到IOC容器里面
    	//1/D初始化
    	//class A{B b ; }
    	//class B { A a ; }
    //5、拿到BeanWrapper 之后， 把BeanWrapper保存到IOC容器中去
    this.factoryBeanInstanceCache.put(beanName , beanWrapper);
    
    //6、执行依赖注入
    populateBean(beanName , new GPBeanDefinition() , beanWrapper);
    
    return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
}

private void populateBean(String beanName , GPBeanDefinition gpBeanDefinition , GPBeanWrapper gpBeanWrapper){
    Object instance = gpBeanWrapper.getWrappedInstance();
    
    Class<?> clazz = gpBeanWrapper.getWrappedClass();
    //判断只有加了注解的类，才执行依赖注入
    if(!(clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class))){
        return ; 
    }
    
    //获得所有的fields
    Field[] fields = clazz.getDeclaredFields();
    for(Field field : fields){
        if(!field.isAnnotationPresent(GPAutowired.class)){
            continue;
        }
        GPAutowired autowired = field.getAnnotation(GPAutowired.class);
        
        String autowiredBeanName = autowired.value().trim();
        if("".equals(autowiredBeanName)){
            autowiredBeanName = field.getType().getName();
        }
        //强制访问
        field.setAccessible(true);
        
        try{
            if(this.factoryBeanInstanceCache.get(autowiredBeanName) == null){continue;}
            field.set(instance , this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
        }catch(IllegalAccessException e){ ...}
    }
    
}
private Object instantiateBean(String beanName , GPBeanDefinition gpBeanDefinition){
    //1、拿到药实例化的对象的类名
    String className = gpBeanDefinition.getBeanCLassName();

    //2、反射实例化， 得到一个对象
    Object instance = null;
    try{
        //假设默认就是单例，细节暂且不考虑，先把主线拉通
        if(this.factoryBeanObjectCache.containsKey(className)){
            instance = this.factoryBeanObjectCache.get(className);
        }else{
            Class<?> clazz = Class.forName(className);
            instance = clazz.newInstance();
            this.factoryBeanObjectCache.put(className , instance);
            this.factoryBeanObjectCache.put(gpBeanDefinition.getFactoryBeanName(), instance);
        }
    }catch(Exception e){...}
    return instance;
}
```

