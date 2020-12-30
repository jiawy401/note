

#### Spring boot 的前世今生

------

spring Framework AOP 、 IOC/DI

Spring 万能胶

- 如何对配置进行轻量化

#### 思考：用springmvc去构建一个web项目发布一个helloWorld的http端口

- 创建一个项目结构（maven/gradle)

- spring的依赖，spring mvc 、servlet api的依赖

- web.xml , DispatcherServlet

- 启动一个Spring MVC的配置，Dispatcher-servlet.xml

- **创建一个Controller 发布一个http请求**

- 发布到jsp/servlet容器

[通用模板]

脚手架工程：可以通过输入相应参数生成一个项目。（目的是为了解决公共重复的工作）

#### Spring的产生

- 2012
- 2013 年spring开始boot的开发
- 2014年发布boot 1.0的版本

- 直到现在2.3的版本

#### 到底什么事springboot

Springboot 可以很容易地创建独立的、基于Spring的生成级别应用程序，你只需运行即可。

我们队Spring平台和第三方持有固化的视图，所以你可以毫不费力的开始，大多数Spring引导应用程序需要最少的Spring配置。

Springboot即Spring framework的一个脚手架

> springboot 是约定优于配置理念下的一个产物

可以集成的容器有以下：

![image-20201228125835321](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201228125835321.png)

-  只要依赖的spring-boot-starter-web的jar， 就会自动内置一个tomcat容器（替换）
- 项目结构
- 默认提供了配置文件application.properties
- starter启动依赖 -  如果是一个webstarter， 默认认为你是去构建一个spring mvc的应用 

#### 如何springmvc 的项目

#### 约定优于配置

#### springboot 如何应用， 集成Mybatis

#### spring boot 和微服务

#### Springboot的特性

- EnableAutoConfiguration   自动装配？

- Starter启动依赖 ，它依赖于自动装配的技术

- Actuator监控 ，提供了一些endpoint 、http、jmx 形式去进行访问，health信息、metrics信息、。。。

- Spring Boot CLI(提供springboot 命令行操作的功能， 用groovy脚本) 有专用的客户端

#### Spring注解驱动的发展过程

------



> Springboot 的特性依赖于 Spring注解驱动

##### Spring Framework的注解驱动的发展历史

###### spring1.x

IOC的功能， 不需要通过new的方式去创建，而是通过bean的方式去管理

```xml
<bean name ="" class="" />
```

###### Spring 2.x的阶段

- @Required ：(注释为为了保证所对应的属性必须被设置**@Required** 注释应用于 bean 属性的 setter 方法，它表明受影响的 bean 属性在配置时必须放在 XML 配置文件中，否则容器就会抛出一个 BeanInitializationException 异常。)

- @Repository(dao):注解的作用不只是将类识别为Bean，同时它还能将所标注的类中抛出的数据访问异常封装为 Spring 的数据访问异常类型。 Spring本身提供了一个丰富的并且是与具体的数据访问技术无关的数据访问异常结构，用于封装不同的持久层框架抛出的异常，使得异常独立于底层的框架。

- @Aspect：作用是把当前类标识为一个切面供容器读取

```java
@Pointcut：Pointcut是植入Advice的触发条件。每个Pointcut的定义包括2部分，一是表达式，二是方法签名。方法签名必须是 public及void型。可以将Pointcut中的方法看作是一个被Advice引用的助记符，因为表达式不直观，因此我们可以通过方法签名的方式为 此表达式命名。因此Pointcut中的方法只需要方法签名，而不需要在方法体内编写实际代码
@Around：环绕增强，相当于MethodInterceptor
@AfterReturning：后置增强，相当于AfterReturningAdvice，方法正常退出时执行
@Before：标识一个前置增强方法，相当于BeforeAdvice的功能，相似功能的还有
@AfterThrowing：异常抛出增强，相当于ThrowsAdvice
@After: final增强，不管是抛出异常或者正常退出都会执行
```

```xml
<!-- 扫码注解包目录com.jiawy-->
<context:component-scan  base-package="com.jiawy" />
```

spring2.5

- @Component(非Service层的组件)
- @Service（service）
- @Controller（controller）
- @RequestMapping 

###### Spring3.x版本

@Configuration  取代配置文件的注解，去xml化

> 核心的目的：把bean对象如何更加便捷的方式去加载到Spring IOC容器中

以下其中一种方式

```java
@ComponentScan("com.jiawy")
@Configuration
public class SpringConfiguration {
    @Bean
    public User user(){
        return new User();
    }
}

public class Demo1{
    public void static main (){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
        sout(applicationContext.getBean(SpringCOnfiguration.class));
    }
}
```

- Component-Scan - 去扫描 @Service @Repository @Controller

- Import 注解

  ```java
  @ComponentScan("com.jiawy")
  @Configuration
  @Import(ImportConfiguration.class)
  public class SpringConfiguration {
      @Bean
      public User user(){
          return new User();
      }
  }
  
  @Configuration
  public  class ImportConfiguration{
  	@Bean 
  	public ImportService importService(){
  		return ImportService();
  	}
  }
  
  public class Demo2{
      public void static main (){
          //ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class , ImportConfiguration.class);
           ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
          sout(applicationContext.getBean(SpringCOnfiguration.class));
      }
  }
  ```

- Enable模块驱动

<img src="C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201228142122844.png" alt="image-20201228142122844" style="zoom:67%;" />

#### Spring3.x版本中，集成Redis或者mybatis

------



- 创建一个配置类
- @bean注解来声明一个bean

```java
@Bean
DefaultKaptcha defaultKaptcha(){
}
```

- @Enable启动一个模块，把相关组件的bean自动装配到IOC容器中

配置文件版：

public class AnnotationDrivenBeanDefinitionParser(implements BeanDefinitionParser).parse(Element element, ParserContext parserContext) ;解析

```xml
//以前启动一个任务步骤1，applicationContext.xml中配置
<task:annotation-driven scheduler="taskSchedulerId" />
<task:scheduler id="taskSchedulerId" pool-size="3" />

```

```java
//步骤2
@Service
public class TestService{
    @Scheduled(fixedRate = 3000)
    public void reportCurrentTime(){
        sout("0000000-===-===-=");
    }
}
public class TestMain{
    main(){
        ApplicationContext context = new FileSystemXmlApplicationContext("classpath:*") ; 
    }
}
```

使用Enable

![image-20201228144149843](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201228144149843.png)

```java
@ComponentScan("扫描路径")
@EnableScheduling //自动完成一些模块下 scheduler  bean的注入
@Configuration
public class TastConfiguration{

}

@Service
public class TestService{
    @Scheduled(fixedRate = 3000)
    public void reportCurrentTime(){
        sout("0000000-===-===-=");
    }
} 

public class TestMain{
    main(){
        ApplicationContext context = new AnnotationCOnfigApplicationContext(TestConfiguration.class); 
    }
}
```



#### Spring boot 的注解驱动

------



##### Spring3.x

无配置化的方式实现Bean的装配

##### Spring 4.x

- @Conditional 条件注解 ，Bean装载的条件化的配置 

```java
@Configuration
public class SpringConfiguration {
    
    /**
     1、在某个环境下装载
     2、或者不满足某个条件的时候，不装载
     3、或者，如果已经装载过了的就不重复装载
    。。。。
    */
    @Conditional(JWCondition.class)
    @Bean
    public User user(){
        return new User();
    }
}

public class JWCondition implements Confition{
    
    @Override
    public boolean matches(ConfitionContext context , AnnotatedTypeMetadata metadata){
        if(1==1){
            return true
        }else{
       		return false;            
        }
    }
}

public class TestMain{
    main(){
        ApplicationContext context = new AnnotationCOnfigApplicationContext(SpringConfiguration.class); 
        sout(context.getBean(User.class));
    }
}
```

##### Spring 5.x

------

@Indexed 用来提升性能的





##### SPring IOC bean的装载

------



- xml
- Configuration
- Enable

![image-20201229143150511](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201229143150511.png)

```java
public class RedisController {


    @Autowired  //在这里能够实现注入的前提是？ IOC存在实例（自动装配）
    private RedisTemplate<String , String > redisTemplate;

    @GetMapping("/say")
    public String say(){
        return   redisTemplate.opsForValue().get("name");
    }
}
```





##### Spring的动态Bean的装载

------



- ImportSelector ：DeferredImportSelector
- Registator: ImportBeanDefinitionRegistrar

springBoot用的AutoConfigurationImportSelector动态加载配置类

```java
public class AutoConfigurationImportSelector implements DeferredImportSelector, BeanClassLoaderAware, ResourceLoaderAware, BeanFactoryAware, EnvironmentAware, Ordered{......}
```

ImportSelector示例：如何动态扫描配置类的，比如：配置了Redis就可以直接注入@Autowired RedisTemplate redisTemplate; 

所有的starter应用下classpath:META-INF/spring.factories文件 ， 存在扫描配置类。

```java
public class JWRedisTemplate { }
------------------------------------------------------------
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWReidsCOnfiguration {
    @Bean
    public JWRedisTemplate redisTemplate(){
        return new JWRedisTemplate();
    }
}
------------------------------------------------------------
public class JWSqlSessionTemplate {}
------------------------------------------------------------
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWMybatisConfiguration {
    @Bean
    public JWSqlSessionTemplate jwSqlSessionTemplate(){
        return new JWSqlSessionTemplate();
    }
}
-------------------------------------------------------------
public class JWDefineImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        // 动态导入bean ， 告诉了Spring， 两个配置类在哪里
        
        //在这里去加载所有的配置类就行？
        //通过某种机制去完成指定路径的配置类的扫描就行？
        //package.class.className
        return new String[]{
JWMybatisConfiguration.class.getName() , JWReidsCOnfiguration.class.getName()
        };
    }
}
----------------------------------------------------------------
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(JWDefineImportSelector.class)
public @interface EnableConfiguration {}
-----------------------------------------------------------------
@SpringBootApplication
@EnableConfiguration
public class SpringBootExampleApplication {
	public static void main(String[] args) {
	ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(SpringBootExampleApplication.class, args);
System.out.println(configurableApplicationContext.getBean(JWRedisTemplate.class));
System.out.println(configurableApplicationContext.getBean(JWSqlSessionTemplate.class));
    }
}
```



#### SPringFactoriesLoader(SPI)

------

spring.factories里面存放配置类扫描的路径。

![image-20201229144333359](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201229144333359.png)



#### SPI机制

------

Service provider interface

满足一下条件：

- 需要在classpath目录下创建一个META-INF/services
- 在该目录下创建一个扩展点的全路径名
  - 文件中填写这个扩展的实现
  - 文件编码格式UTF-8

- ServiceLoader去进行加载



扩展链实现

SPI的扩展定义组件，第三方去扩展 比如：

![image-20201229145906570](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201229145906570.png)

实例：

//DataBaseDriverSpi工程==============================================start

```java
public interface DataBaseDriver {

    String buildConnect(String host);
} 
```

//DataBaseDriverSpi工程==============================================end

//驱动工程mysqlDriverSPI===========================================start

```java
public class MysqlDriver  implements DataBaseDriver {

    @Override
    public String buildConnect(String s) {
        return "Mysql的驱动实现：" +s;
    }
}
```

POM.XML

```xml
    <dependency>
      <groupId>com.spi</groupId>
      <artifactId>DataBaseDriverSpi</artifactId>
      <version>1.0-SNAPSHOT</version>
    </dependency>
```

在resources下建对应的配置文件 ：META-INF.services

文件名要与DataBaseDriver包名一致。

文件名：com.spi.DataBaseDriver

```
com.spi.mysql.MysqlDriver
```

//驱动工程mysqlDriverSPI===========================================end

//驱动应用程序appSPI==============================================start

```java
public class App
{
    public static void main( String[] args )
    {
        ServiceLoader<DataBaseDriver> serviceLoader = ServiceLoader.load(DataBaseDriver.class);
        for(DataBaseDriver dataBaseDriver : serviceLoader){
            System.out.println(dataBaseDriver.buildConnect("test"));
        }
        System.out.println( "Hello World!" );
    }
}
```

控制台打印：

![image-20201229152932468](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201229152932468.png)

pom.xml

```java
<dependency>
    <groupId>com.spi.mysql</groupId>
    <artifactId>mysql-Driver</artifactId>
    <version>1.0-SNAPSHOT</version>
    </dependency>

    <dependency>
    <groupId>com.spi</groupId>
    <artifactId>DataBaseDriverSpi</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

//驱动应用程序appSPI==============================================end

#### Spring boot 自动装配

------

THE END

#### 条件控制

------

对于官方的starter组件，

- 官方包spring-boot-starter-xxx
- 第三方包 xxx-spring-boot-starter

#### 条件装配

- 在类上加注解@ConditionalOnClass
- 在/resourses//META-INF/spring-autoconfigure-metadata.properties中加，没有新建，示例：com.jiawy.autoconfiguration.ConditionalOnClass=com.jiawy.DemoClass

#### Starter组件的原理

------



自动装配与Starter组件有非常强的关联性。

 optional标签为true不会传递引用

```java
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-parent</artifactId>
     <version>2.4.1</version>
     <optional>true</optional>
     </dependency>
```





#### Actuator



















