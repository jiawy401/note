## 课程目标

1. 了解看源码最有效的方式，先猜测后验证，不要一开始就去调试代码
2. 浓缩就是精华，用300行最简洁的diam提炼Spring的基本设计思想
3. 掌握spring框架基本脉络

## 内容定位

1. 具有1年以上的SpringMVC使用经验
2. 希望深入了解Spring源码的人群，对Spring有一个整体的宏观感受
3. 全程手写实现SpringMVC的核心功能，从最简单的V1版本一步一步优化为V2版本，最后到V3版本。

## 实现思路

介绍下Mini版本的Spring基本实现思路，如下图所示：

![image-20210420135436250](process\image-20210420135436250.png)

## 自定义配置

### 配置application.properties文件

为了解析方便，我们用application。properties来代替application.xml文件，具体配置内容如下：

```xml
scanPackage=com.gupaoedu.demo
```

### 配置web.xml文件

大家都知道，所有依赖于web容器的项目，都是从读取web.xml文件开始的。我们先配置好web.xml中的内容

```xml
<?xml version = "1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://java.sun.com/xml/ns/j2ee"
         xmlns:javaee="http://java.sun.com/xml/ns/javaee"
         xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd"
         version="2.4">
    <display-name>GUpao web application</display-name>
    <servlet>
    	<servlet-name>gpmvc</servlet-name>
        <servlet-class>com.gupaoedu.mvcframework.servlet.v2.GPDispatcherServlet</servlet-class>
        <init-param>
        	<param-name>contextConfigLocation</param-name>
            <param-value>application.properties</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
    	<servlet-name>gpmvc</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
</web-app>

```

其中GPDispatcherServlet是自己模拟Spring实现的核心功能类。

### 自定义Annotation

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

### 配置Annotation

配置业务实现类DemoService:

```java
import com.gupaoedu.demo.service.IDemoService;
import com.gupaoedu.mvcframework.annotation.GPService;
@GPService
public class DemoService implements IDemoService{
    public  String get(String name){
        return "My name is " + name ;
    }
}
```

配置请求入口类：DemoAction：

```java
import java.io.IOException; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gupaoedu.demo.service.IDemoService;
import com.gupaoedu.mvcframework.annotation.GPAutowired;
import com.gupaoedu.mvcframework.annotation.GPController;
import com.gupaoedu.mvcframework.annotation.GPRequestMapping;
import com.gupaoedu.mvcframework.annotation.GPRequestParam;
@GPController
@GPRequestMapping("/demo")
public class DemoAction{
    @GPAutowired 
    private IDemoService demoService;
    @GPRequestMapping("/query")
    public void query(HttpServletRequest req , HttpServletResponse resp,
                     @GPRequestParam("name") String name , @GPRequestParam("id") String id){
        String result = "my name is " + name + ",id = " +id;
        try{
            resp.getWriter().write(result);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @GPRequestMapping("/add")
    public void add(HttpServletRequest req, HttpServletResponse resp,
                  @GPRequestParam("a") Integer a , @GPRequestaParam("b")Integer b ){
        try{
            resp.getWriter().write(a + " + " + b + " = " (a+b));
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    @GPRequestMapping("/remove")
    public void remove(HttpServletRequest req, HttpServletResponse resp,
                      @GPRequestParam("id") Integer id ){
        
    }
}
```

## 容器初始化

### 实现V1版本

所有的核心逻辑全部写在一个init()方法中。

```java
import com.gupaoedu.mvcframework.annotation.GPAutowired;
import com.gupaoedu.mvcframework.annotation.GPController;
import com.gupaoedu.mvcframework.annotation.GPRequestMapping;
import com.gupaoedu.mvcframework.annotation.GPService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
public class GPDispatcherServlet extends HttpServlet{
    private Map<String,Object> mapping = new HashMap<String,Object>();
    
    @Override
    protected void doGet(HttpServletRequest req , HttpServletResponse resp) throws ServletException , IOException {this.doPost(req.resp);}
    @Override
    protected void doPost(HttpServletRequest req , HttpServletResponse resp ) throws ServletException , IOException {
    	try{
            doDispatch(req,resp);
        }catch(Exception e){
            e.printStackTrace();
            resp.getWriter().write("500 Exception " + Arrays.toString(e.getStackTrace()));
        }
    }
    
    private void doDispatch(HttpServletRequest req , HttpServletResponse resp) throws Exception{
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath , "").replaceAll("/+","/");
        if(!this.mapping.containsKey(url)){resp.getWriter().write("404 Not Found !") ; return;}
        Method method = (Method) this.mapping.get(url);
        method.invoke(this.mapping.get(method.getDeclaringClass().getName()) , new Object[]{req,resp,params.get("name")[0]});
    }
    
    @Override
    public void init(ServletConfig config ) throws ServletException{
        InputStream is = null ; 
        try{
            Properties configContext = new Properties();
            is = this.getClass().getClassLoader()
                .getResourceAsStream(
                  config.getInitParameter("contextConfigLocation")
            	);
            configContext.load(is);
            String scanPackage =
                configContext.getProperty("scanPackage");
            doScanner(scanPackage);
            for(String className : mapping.keySet()){
                if(!className.contains(".")){continue;}
                Class<?> clazz = Class.forName(className);
                if(clazz.isAnnotationPresent(GPController.class)){
                    mapping.put(className,clazz.newInstance());
                    String baseUrl = "";
                    if(clazz.isAnnotationPresent(GPRequestMapping.class))
                    {
                    	GPRequestMapping requestMapping = 
                            clazz.getAnnotation(GPRequestMapping.class);
                        baseUrl = requestMapping.value();
                    }
                    Method[] methods = clazz.getMethods();
                    for(Method method : methods){
                        if(!method.isAnnotationPresent(GPRequestMapping.class)){continue;}
                        GPRequestMapping requestMapping = 
                            method.getAnnotation(GPRequestMapping.class);
                        String url = (baseUrl + "/" + requestMapping.value().replaceAll("/+"  , "/"));
                        mapping.put(url , method);
                        System.out.println("Mapped "  + url + ", " + method);
                        
                    }
                }else if(clazz.isAnnotationPresent(GPService.class)){
                    GPService service = 
                        clazz.getAnnotation(GPService.class);
                    String beanName = service.value();
                    if("".equals(beanName)){beanName = clazz.getName();}
                    Object instance = clazz.newInstance();
                    mapping.put(beanName , instance);
                    for(Class<?> i : clazz.getInterface()){
                        mapping.put(i.getName() , instance);
                    }
                } else {continue;}
                
            }
            for(Object object : mapping.values()){
                if(object == null ){continue;}
                Class clazz = object.getClass();
                if(clazz.isAnnotationPresent(GPController.class)){
                    Field[] fields = clazz.getDeclaredFields();
                    for(Field field : fields){
                        if(!field.isAnnotationPresent(GPAutowired.class))
                        {continue;}
                        GPAutowired autowired = field.getAnnotation(GPAutowired.class);
                        String beanName = autowired.value();
                        if("".equals(beanName)){
                            beanName = field.getType().getName();
                        }
                        field.setAccessible(true);
                        try{
                            field.set(mapping.get(clazz.getName()) ,mapping.get(beanName));
                            
                        }catch(IllegalAccessException e ){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch(Exception e){
            
        }finally{
            if(is != null ){
                try{is.close();}catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        System.out.println("GP MVC Framework is init ");
    }
    
    private void doScanner(String scanPackage){
        URL url = this.getClass().getClassLoader().getResource("/"  + scanPackage.replaceAll("\\." , "/"));
        File classDir = new File(url.getFile());
        for(File file : clazzDir.listFiles()){
            if(file.isDirectory()){
                doScanner(scanPackage + "." + file.getName());
            }else{
                if(!file.getName().endWith(".class")){continue;}
                String clazzName = (scanPackage + "." + file.getName().replace(".class" , ""));
                mapping.put(clazzName , null);
            } 
        }
    }
    
}
```

> clazz.isAnnotationPresent()  **java.lang.Package.isAnnotationPresent(Class<? extends Annotation> annotationClass)** 方法返回true，如果指定类型的注释存在于此元素上,。例如： **A.isAnnotationPresent(B.class)；意思就是：****注释B是否在此A上。****如果在则返回true；不在则返回false。**

### 实现V2版本

在V1版本上进行了优化，采用了常用的设计模式（工厂模式、单例模式、委派模式、策略模式），将init() 方法中的代码进行封装。按照之前的实现思路，先搭建基础框架，再注入血肉，具体代码如下:

```java
public void init(ServletConfig config ) throws ServletException{
    //1 加载配置文件
    doLoadConfig(config.getInitParameter("contextConfigLocation"));
    //2 扫描相关的类
    doScanner(contextConfig.getProperty("scanPackage"));
    //3 初始化扫描到的类，并且放入到IOC容器之中
    doInstance();
    //4 完成自动化的依赖注入
    doAutowired();
    //5 初始化 HandleMapping
    doInitHandlerMapping();
    
    System.out.println("GP Spring framework is init .");
}
```

声明全局的成语变量，其中IOC容器就是注册时单例的具体案例：

```java
//保存application.properties配置文件中的内容
private Properties contextConfig = new Properties();
//保存扫描的所有的类名
private List<String> classNames = new ArrayList<String>();
//传说中的IOC容器，我们来揭开他的面纱
//为了简化程序，暂时不考虑ConcurrentHashMap
//主要还是关注设计思想和原理
private Map<String , Object> ioc = new HashMap<String,Object>();
//保存url 和Method 的对应关系
private Map<String , Method> handlerMapping = 
    new HashMap<String,Method>();
```

实现doLoadConfig()方法：

```java
//加载配置文件
private void doLoadConfig(String contextConfigLocation){
    //直接从类路径下找到Spring主配置文件所在的路径
    //并且将其读取出来放到Properties对象中
    //相对于scanPackage = com.gupaoedu.demo 从文件中保存到了内存中
    InputStream is = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
    try{
        contextConfig.load(is);
    }catch(IOException e){
        e.printStackTrace();
    }finally{
        if(null != is) {try{is.close()}catch(IOException e){e.printStackTrace();}}
    }
}
```

实现doScanner()方法：

```java
//扫描出相关的类
private void doScanner(String scanPackage){
    URL url = this.getClass().getClassLoader().getResource("/"  + scanPackage.replaceAll("\\." ,"/"));
    //scanPackage = com.gupaoedu.demo 存储的是包路径
    //转换为文件路径，实际上就是把.替换为/就OK了
    //classpath 下不仅有.class 文件， .xml文件 .properties文件
    File classPath = new File(url.getFile());
    for(File file : classPath.listFiles()){
        if(file.isDirectory()){
            doScanner(scanPackage + "." + file.getName());
        }else{
            //变成包名.类名
            //Class.forName();
            if(!file.getName().endsWith(".class")){continue;}
            classNames.add(scanPackage + "." + file.getName().replace(".class" , ""));
        }
    }
}
```

实现doInstance()方法，doInstance()方法就是工程模式的具体实现：

```java
private void doInstance(){
    if(classNames.isEmpty()){return;}
    try{
        for(String className : classNames){
            Class<?> clazz = Class.forName(className);
            //什么样的类才需要初始化呢？
            //加了注解的类，才初始化，怎么判断？
            //为了简化代码逻辑，主要体会设计思想，只举例@Controller和@Service，
            //@Componment...就一一举例了
            if(clazz.isAnnotationPresent(GPController.class)){
                Object instance = clazz.newInstance();
                String beanName = toLowerFirstCase(clazz.getSimpleName());
                //key-value
                //class类名的首字母小写
                ioc.put(beanName , instance);
                
            }else if(clazz.isAnnotationPresent(GPService.class)){
                //1 默认就根据beanName 类名首字母小写
                String beanName = toLowerFirstCase(clazz.getSimpleName());
                //2 使用自定义的beanName
                GPService service = clazz.getAnnotation(GPService.class);
                if(!"".equals(service.value())){
                    beanName = service.value();
                }
                Object instance = clazz.newInstance();
                ioc.put(beanName , instance);
                //3 根据包名.类名作为beanName
                for(Class<?> i : clazz.getInterfaces()){
                    if(ioc.containsKey(i.getName())){
                        throw new Exception("The beanName is exists!");
                    }
                    ioc.put(i.getName() , instance);
                }
            }else {continue;}
        }
    }catch (Exception e){
        e.printStackTrace();
    }
}
```

为了处理方便，自己实现了toLowerFirstCase方法， 来实现类名首字母小写，具体代码如下：

```java
//如果类名本身是小写字母，确实会出问题
//但是我要说明的是，这个方法是我自己用，private的
//传值也是自己传，类也都遵循了驼峰命名法
//默认传入的值 ， 存在首字母小写的情况，也不可能出现非字母的情况
//为了简化程序逻辑，就不做其他判断了， 大家了解就OK
//其实用写注释的视角都能把逻辑都写完了
private String toLowerFirstCase(String simpleName){
    char[] chars = simpleName.toCharArray();
    //之所以加， 是因为大小写字母的ASCII码相差32
    //而且大写字母的ASCII码要小于小写字母的ASCII码
    //在java中， 对char做算学运算，实际上就是对ASCII码做运算
    chars[0] += 32;
    return String.valueOf(chars);
}
```

实现doAutowired() 方法：

```java
private void doAutowired(){
    if(ioc.isEmpty()){return;}
    for(Map.Entry<String,Object> entry : ioc.entrySet()){
        //拿到实例的所有的字段
        //Declared所有的，特定的，字段，包括private/protected/default
        //正常来说，普通的OOP编程只能拿到public的属性
        Field[] fields = entry.getValue().getClass().getDeclaredFields();
        for(Field field : fields){
            if(!field.isAnnotationPresent(GPAutowired.class)){
                continue;
            }
            GPAutowired autowired = field.getAnnotation(GPAutowired.class);
            //如果用户没有自定义beanName ，默认就根据类型注入。
            //这个地方省去了对类名首字母小写的情况的判断，这个作为课后作业
            //小伙伴们自己去完善
            String beanName = autowired.value().trim();
            if("".equals(beanName)){
                //获得接口的类型，作为key待会拿这个key到ioc容器中取值
                beanName = field.getType().getName();
            }
            //如果是public以外的修饰符，只要加了@Autowired注解，都要轻质赋值
            //反射中叫做暴力访问，
            field.setAccessible(true);
            
            //反射调用的方式
            //给entry.getValue()这个对象的field字段，赋ioc.get(beanName)这个值
            try{
                field.set(entry.getValue() , ioc.get(beanName));
            }catch(IllegaAccessException e){
                e.printStackTrace();
                continue;
            }
        }
    }
}
```

实现doInitHandlerMapping()方法，handlerMapping就是策略模式的应用案例：

```java
//初始化url和Method的一对一对应关系
private void doInitHandlerMapping(){
    if(ioc.isEmpty()){return ; }
    for(Map.Entry<String , Object> entry : ioc.entrySet()){
        Class<?> clazz = entry.getValue().getClass();
        if(!clazz.isAnnotationPresent(GPController.class)){continue;}
        //保存写在类上面的@GPRequestMapping("/demo")
        String baseUrl = "";
        if(clazz.isAnnotationPresent(GPReqeustMapping.class)){
            GPRequestMapping requestMapping = 
                clazz.getAnnotation(GPRequestMapping.class);
            baseUrl = requestMapping.value();
        }
        //默认获取所有的public方法
        for(Method method : clazz.getMethods()){
            if(!method.isAnnotationPresent(GpRequestMapping.class)){continue;}
            GPRequestMapping reqeustMapping = 
                method.getAnnotation(GPRequestMapping.class);
            //demoquery
            //  /demo/query
            String url = ("/"  + baseUrl + "/" + requestMapping.value().replaceAll("/+" , "/"));
            handlerMapping.put(url , method);
            System.out.println("Mapped " + url + " , " + method);
        }
    }
}
```

到这里为止初始化阶段就已经完成，接下来实现运行阶段的逻辑，来看doPost/doGet的代码：

```java
@Override
protected void doGet(HttpServletRequest req , HttpServletResponse resp ) throws ServletException , IOException {
    this.doPost(req,resp);
}
@Override
protected void doPost(HttpServletRequest req , HttpServletResponse resp) throws ServletException , IOException {
    //6 根据url 调用method 
    try{
        doDispatch(req , resp);
        
    }catch(Exception e ){
        e.printStackTrace();
        resp.getWriter().write("500 Exception , Detail :" +Arrays.toString(e.getStackTrace()));
    }
}
```

doPost()方法中用了委派模式，委派模式的具体逻辑在doDispatch()方法中：

```java
private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception{
    String url = req.getRequestURI();
    String contextPath = req.getContextPath();
    url = url.replaceAll(contextPath , "").replaceAll("/+" ,"/");
    if(!this.handlerMapping.containsKey(url)){
        resp.getWriter().write("404 not found!");
        return ; 
    }
    Method method = this.handlerMapping.get(url);
    Map<String , String[]> paramsMap = req.getParameterMap();
    String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
    method.invoke(ioc.get(beanName) , new Object[]{req,resp , paramsMap.get("name")[0]});
}
```

在以上代码中，doDispatch()虽然完成了动态委派并反射调用，但对url参数处理还是静态代码。要实现url参数的动态获取，其实还稍微有些复杂。我们可以优化doDIspatch()方法的实际逻辑，代码如下：

```java
private void doDispatch(HttpServletRequest req , HttpServletResponse resp) throws Exception {
    String url = req.getRequestURI();
    String contextPath = req.getContextPath();
    url = url.replaceAll(contextPath , "").replaceAll("/+" , "/");
    if(!this.handlerMapping.containsKey(url)){
        resp.getWriter().write("404 not found !");
        return ;
    }
    Method method = this.handlerMapping.get(url);
    Map<String , String[]> paramMap = req.getParameterMap();
    //实参列表
    //实参列表要根据形参列表才能决定，首先得拿到形参列表。
    Class<?> paramterTypes = method .getParameterMap();

    Object[] paramValues = new Object[paramterTypes.length];
    for(int i = 0 ;i < parameterTypes.length; i ++){
        Class parameterType = parameterType[i];
        if(parameterType == HttpServletRequest.class){
            parameValues[i] = req;
            continue;
        }else if(parameterType == HttpServletResponse.class){
            parameValues[i] = resp;
            continue;
        }else if(parameterType == String.class){
            Annotation[][] pa = method.getParameterAnnotations();
            for(int j = 0 ; j < pa.length ; j ++){
                for(Annotation a : pa[i]){
                    if(a instanceof GPReqeustParam){
                        String paramName = ((GPReqeustParam) a).value();
                        if(!"".equals(paramName.trim())){
                            String value = 
                                Arrays.toString(paramsMap.get(paramName))
                                .replaceAll("\[|\\]" , "")
                                .replaceAll("\\s" , ",");
                            parameValues[i]  = value;
                        }
                    }
                }
            }
        }
    }
    String beanName = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
    method.invoke(ioc.get(beanName) , parameValues);
}
```

### 实现V3版本

首先改造HandlerMapping，在真实的Spring源码中，HandlerMapping其实是一个List而非Map。List中的元素是一个自定义的类型。

```java
/**
Handler记录Controller中的RequestMapping和Method的对应关系
内部类
*/
private class Handler{
    protected Object controller ; //保存方法对应的实例
    protected Method method ;     //保存映射的方法
    protected Pattern pattern ;   //${} url 占位符解析
    protected Map<String , Integer> paramIndexMapping ; //参数顺序
    /** 构造一个Handler基本的参数
    *@param controller
    *@Param method
    */
    protected Handler(Pattern pattern , Object controller, Method method){
        this.controller = controller;
        this.method = method;
        this.pattern = pattern ; 
        
        paramIndexMapping = new HashMap<String , Integer>();
        putParamIndexMapping(method);
    }
    
    private void putParamIndexMapping (Method method){
        //提取方法中加了注解的参数
        Annotation[][] pa = method.getParameterAnnotations();
        for(int i = 0 ;i < pa.length ; i ++){
            for(Annotation a : pa[i]){
                if(a instanceof GPRequestParam){
                    String paramName = ((GPRequestParam) a).value();
                    if(!"".equals(paramName.trim())){
                        paramIndexMapping.put(paramName , i);
                    }
                }
            }
        }
        //提取方法中的request和response参数
        Class<?> [] paramsTypes = method.getParameterTypes();
        for(int i = 0 ;i < paramsTypes.length; i ++){
            Class<?> type = paramsTypes[i];
            if(type == HttpServletRequest.class ||
              type == HttpServletResponse.class){
                paramIndexMapping.put(type.getName() , i);
            }
        }
    }
    
}

```

然后，优化HandlerMapping的结构，代码如下：

```java
//保存所有的Url和方法的映射关系
private List<Handler> handlerMapping = new ArrayList<Handler>();
```

修改doInitHandlerMapping() 方法：

```java
private void doInitHandlerMapping(){
    if(ioc.isEmpty()){return;}
    for(Entry<String,Object> entry : ioc.entrySet()){
        Class<?> clazz = entry.getValue().getClass();
        if(!clazz.isAnnotationPresent(GPController.class)){continue;}
        String url = "";
        //获取Controller的url配置
        if(clazz.isAnnotationPresent(GPRequestMapping.class)){
            GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
            url = requestMapping.value();
        }
        //获取Method的url配置
        Method[] methods= clazz.getMethods();
        for(Method method : methods){
            //没有加RequestMapping 注解的直接忽略
            if(!method.isAnnotationPresent(GPRequestMapping.class)){
                continue;
            }
            //映射URL
            GPRequestMapping requestMapping = method.getAnnotation(GPRequestMapping.class);
            String regex = ("/" + url + requestMapping.value()).replaceAll("/+" ,"/");
            Pattern pattern = Pattern.compile(regex);
            handlerMapping.add(new Handler(pattern,entry.getValue(),method));
            System.out.println("mapping " + regex + "," + method);
        }
    }
}
```

修改doDispatch()方法：

```java
/**
*匹配URL
*@Param req
*@param resp
*@return 
*/
private viod doDispatch(HttpServletRequest req, HttpServletResponse resp)throws Exception {
    try{
        Handler handler = getHandler(req);
        if(handler == null){
            //如果没有匹配上，返回404错误
            resp.getWriter().write("404 Not Found");
            return ; 
        }
        //获取方法参数列表
        Class<?> [] paramTypes = handler.method.getParameterTypes();
        
        //保存所有需要自动赋值的参数值
        Object[] paramValues = new Object[paramTypes.length];
        Map<String,String[]>  params = req.getParameterMap();
        for(Entry<String , String[]> param : params.entrySet()){
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]" , "").replaceAll(",\\s",",");
            
            //如果找到匹配的对象，则开始填充参数值
            if(!handler.paramIndexMapping.containsKey(param.getKey())){continue;}
            int index = handler.paramIndexMapping.get(param.getKey());
            paramValues[index] = convert(paramTypes[index] , value);
        }
        //设置方法中的request和response对象
        int  reqIndex = handler.paramIndexMapping.get(HttpServletRequest.class.getName());
        paramValues[reqIndex] = req;
        int respIndex = handler.paramIndexMapping.get(HttpServletResponse.class.getName());
        paramValues[respIndex] = resp;
        handler.method.invoke(handler.controller, paramValues);
        
    }catch(Exception e){
        throw e;
    }
}

private Handler getHandler(HttpServletRequest req) throws Exception{
    if(handlerMapping.isEmpty()){return null;}
    String url = req.getRequestURI();
    String contextPath = req.getContextPath();
    url = url.replace(contextPath , "").replaceAll("/+"  , "/");
    for(Handler handler : handlerMapping){
        try{
            Matcher matcher = handler.pattern.matcher(url);
            //如果没有匹配上继续下一个匹配
            if(!matcher.matches()){continue;}
            return handler ; 
        }catch(Exception e){
            throw e ; 
        }
        return null;
    }
}

//url传过来的参数都是String类型，HTTP是基于字符串协议
//只需要把String转换成为任意类型就好
private Object convert(Class<?> type , String value){
    if(Integer.class == type){
        return Integer.valueOf(value);
    }
    //如果还有double或者其他类型，继续加if
    //这时候，我们应该想到策略模式了
    //在这里暂时不实现，希望小伙伴自己来实现
    return value;
}
```

在以上代码中，增加了两个方法，一个是getHandler() 方法， 主要负责处理url的正则匹配 ；  一个是convert()方法，主要负责url参数强制类型转换。

![image-20210425150749080](process\image-20210425150749080.png)