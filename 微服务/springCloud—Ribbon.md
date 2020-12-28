# Spring CLoud

- Spring Cloud Netflix 			NetFlix  OSS(Ribbon/Hystrix/Eureka)


- Spring CLoud Alibaba		   (dubbo/nacos/seata/rocketMQ/sentinel)




### Spring Cloud Netflix Ribbon



### 微服务架构下的服务通信需求



### 通信如何实现

- RPC框架； dubbo / ..

- 手写RPC



### RESTful HTTP协议通信

规范了HTTP通信协议的标准

- HTTP METHOD 约束资源操作类型 GET POST PUT DELETE 

- REST是面向资源的 

   /order（GET)       /order/${id}

  /order(POST)        

  /order(PUT)

  /order/${id} (DELETE)

  /orders

- 名词， /queryOrderById

- HTTP返回码2xxx 3xxxx  4xxxx  5xxxxx



### 服务的拆分

------

#### 拆分的背景

- 系统已经运行了很长时间。

- 一开始就采用微服务架构

  > 架构永远在变

#### 拆分的前提

收益

- 问题驱动 
- 前置化的规划
- 性能提升
- 团队（运维）是否能支撑

准备工作

- 基础设施的完善
- 业务模型和整体架构的规划（数据库的拆分、应用层的优化）



#### 拆分的准备



#### 微服务怎么拆分

<边界>

- 功能维度
- 业务维度
- 康威定律（考虑团队结构的）
- 拆分粒度（商品服务（商品库存、商品目录、商品评论、商品图片）
- PerfMa

```

```



### RestTemplate

```java
@Autowired
RestTemplate restTemplate; 
@Bean
public RestTemplate restTemplate(){
	retrun new RestTemplate();
}
@Bean
public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
	retrun restTemplateBuilder.build();
}

@GetMapping("/user/{id}")
public String findById(@PathVariable("id") int id){
	//调用订单服务获得订单信息
	//HttpClient   RestTemplate OkHttp  JDK HttpUrlCOnnection
	return restTemplate.getForObject(url , String.class);
}
```

##### @Qualifier 标记的概念

@LoadBalanced的本质是Qualifier的注解，给LoadBalanced一个标记，LoadBalancerAutoConfiguration自动装配类。

举例：

```java
public class TestCOnfiguration{
	
	@Qualifier
	@Bean("testClass1")
	TestClass testClass1(){
		return new TestClass("TestClass1");
	}
	@Qualifier
	@Bean("testClass2")
	TestClass testClass2(){
		return new TestClass("TestClass2");
	}
}
```

```java
public class TestController{
    @Qualifier
    @Autowired
    List<TestClass> testClassList = Collections.emptyList();
    
    @GetMapping("/test")
    public Object test(){
        return testClassList;
    }
}
```

![image-20201216170454890](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201216170454890.png)



![image-20201216172455473](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201216172455473.png)



![image-20201216172400906](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201216172400906.png)

![image-20201217130742261](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201217130742261.png)

![image-20201217132451624](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201217132451624.png)

![image-20201217132801987](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201217132801987.png)



### Ribbon:

1. 解析配置中的服务器列表
2. 基于负载均衡的算法来实现请求的分发



### Ribbon负载均衡

- 客户端的负载，地址列表的请求与转发
- spring-cloud-starter-netflix-ribbon

 

引用spring-cloud-starter-netflix-ribbon。jar包

配置文件配置：application.properties

```java
spring.application.name = spring-cloud-order-service

management.endpoints.jmx.exposure.include=*
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details = always

alibaba.cloud.access-key = ****
alibaba.cloud.secret-key=****

service.port=8080

#配置指定服务的提供者的地址列表
sprint-cloud-order-service.ribbon.listOfServers=\
	localhost:8080,localhost:8082

```



```java
@Autowired
LoadBalancerClient loadBalancerClient;
@Bean
public RestTemplate restTemplate(){
	retrun new RestTemplate();
}
@GetMapping("/user/{id}")
public String findBYId(@PathVariable("id") int id){
	ServiceInstance serviceInstance = loadBalancerClient.choose("spring-cloud-order-service");
	String url = String.format("http://%s:%s" , serviceInstance.getHost(),serviceInstance.getPort()+"/order");
	return restTemplate.getForObject(Url,String.class)
}
```

注解的方式

```java
@Bean
public RestTemplate restTemplate(){
	retrun new RestTemplate();
}
@Bean
@LoadBalanced
public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
	retrun restTemplateBuilder.build();
}
@GetMapping("/user/{id}")
public String findBYId(@PathVariable("id") int id){
	return restTemplate.getForObject("http://spring-cloud-order-service/orders",String.class);
}
```



### Ribbon源码分析

RestTemplate.doExecute

​	ClientHttprequestFactory request = this.getRequestFactory().createRequest(URI , HttpMothod)

码1

```java
protected <T> T doExecute(URI url , @Nullable HttpMethod method, @Nullable RequestCallback requestCallback , @Nullable ResponseExtractor<T> request){
    Assert.notNull (url , "URI is required");
    Assert.notNull(method , "HttpMethod is required ");
    ClientHttpResponse response = null;
    
    Object var14; 
    try{
        //码2
        ClientHttpRequest request = this.createRequest(url , method );
        if(requestCallback != null ){
            requestCallback.doWithRequest(request);
        }
        //码3
        response = request.execute(); 
    }catch(){
        
    }
}
```

码2

```java

AbstractClientHttpRequestFactoryWrapper.java
protected ClientHttpRequest createRequest(URI url , HttpMethod method ) throws IOException{
    ClientHttpRequest request = this.getRequestFactory().createRequest(url,method);
    this.initialize(request);
    if(this.logger.isDebugEnabled()){
        this.logger.debug("HTTP" + method.name() +" " +url);
    }
    return request;
}

InterceptingClientHttpRequest.java extends AbstractBufferingClientHttpRequest.java  extends AbstractClientHttpRequest.java
    
protected ClientHttpRequest createRequest(URI uri , HttpMethod httpMethod,  ClientHttpRequestFactory requestFactory ){
    return new InterceptingCLientHttpRequest(requestFactory , this.interceptors,uri , htptMethod);
}

//AbstractClientHttpRequest中的executeInternal方法
@OVerride
protected final ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutPut) throws IOException{
    InterceptingRequestExecution requestExecution = new InterceptingRequestExecution();
    return requestExecution.execute(this, bufferedOutput);
}

```

![image-20201225141544472](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201225141544472.png)

码3

```
AbstractClientHttpRequest.java
public final ClientHttpResponse execute() throws IOException {
	this . assertNotExecuted();
	ClientHttpResponse result = this.executeInternal(this.headers);
	this.executed = true; 
	return result ; 
}


```

![image-20201225145013623](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201225145013623.png)

RibbonClientConfiguration.java

![image-20201225145327703](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201225145327703.png)

![image-20201225152132122](C:\Users\xsk\AppData\Roaming\Typora\typora-user-images\image-20201225152132122.png)

setPingInertval 会不断的 检查服务列表