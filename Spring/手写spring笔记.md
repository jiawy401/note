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
</web-app>

```

