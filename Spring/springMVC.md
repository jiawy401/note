## MVC顶层设计

GPDispatcherServlet请求调度

```java
import spring.framework.annotation.GPController;
import spring.framework.annotation.GPReqeustMapping;
import spring.framework.context.GPApplicationContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class GPDispatcherServlet extends HttpServlet{
    private final String CONTEXT_CONFIG.LOCATION = "contextConfigLocation";
    
    private GPApplicationContext context;
    
    private List<GPHandlerMapping> handlerMappings = 
        new ArrayList<GPHandlerMapping>();
    
    private Map<GPHandlerMapping , GPHandlerAdater> handlerAdapters = 
        new HashMap<GPHandlerMapping , GPHandlerAdapter>();
    
    private List<GPViewResolver> viewResolvers = 
        new ArrayList<GPViewResolver>();
    
    @Override
    protected void doGet(HttpServletReqeust req, HttpServletResonse resp) throw ServletException , IOException{
        this.doPost(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req , HttpServletResponse resp) throws ServletException , IOException {
        try{
            this.doDispatch(req,resp);
        }catch(Exception e){
            resp.getWriter().write("500 Exception , Details:\r\n " +Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }
    
    private void doDispatch(HttpServletRequest req , HttpServletResponse resp) throws Exception{
        //1、通过从request中拿到URL， 去匹配一个HandlerMapping
        GPHandlerMapping handler = getHandler(req);
        
        if(handler == null){
            processDispatchResult(req, resp,new GPModelAndView("404"));
            return ;
        }
        
        //2、准备调用前得参数
        GPHandlerAdapter ha = getHandlerAdapter(handler); 
        
        //3、真正的调用方法，返回ModelAndView 存储了要传回页面上的值，和页面模板的名称
        GPModelAndView mv = ha.handle(req,resp,handler);
        //这一步才是真正的输出
        processDispatchResult(req,resp.mv);
    }
    
    private void processDispatchResult(HttpServletReqeust req , HttpServletResponse resp, GPModelAndView mv) throws Exception {
        //把给我的ModelAndView 变成一个HTML、OutputStream、 json、framework、velocity
        //ContextType
        if(null == mv ) {return ; }
        
        //如果ModelAndView 不为null,怎么办？
        if(this.viewResolvers.isEmpty() ){return ;}
        
        for(GPViewResolver viewResolver : this.viewResolvers){
            GPView view = viewResolver.resolveViewName(mv.getViewName(),null);
            view.render(mv.getModel(),req,resp);
            return ;
        }
        
    }
    
    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handler){
        if(this.handlerAdapters.isEmpty()){return null;}
        
        GPHandlerAdapter ha = this.handlerAdapters.get(handler);
        if(ha.supports(handler)){
            return ha;
        }
        return null;
    }
    
    private GPHandllerMapping getHandler(HttpServletRequest req) throws Exception {
        if(this.handlerMappings.isEmpty()){return null;}
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath , "").replaceAll("/+" ,"/");
        for(GPHandlerMapping handler : this.handlerMappings){
            try{
                Matcher matcher = handler.getPattern().matcher(url);
                //如果没有匹配上继续下一个匹配
                if(!matcher.matches()){continue;}
                return handler;
            }catch(Exception e){...}
        }
        return null;
    }
    
    @Override
    public void init(ServletConfig config) throws ServletException{
        //1、初始化ApplicationContext
        context = new GPApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        //2、初始化Spring MVC九大组件
        initStrategies(context);
    }
    
    
    //初始化策略
    protected void initStrategies(GPApplicationContext context){
        //handlerMapping 必须实现
        initHandlerMapping(context);
        //初始化参数适配器，必须实现
        initHandlerAdapters(context);
        //初始化视图转换器，必须实现
        initViewResolvers(context);
    }
    
    private void initViewResolvers(GPApplicationContext context){
        //拿到模板的存放目录
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        
        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();
        for(int i = 0 ;i < templates.length ; i ++){
            //这里主要是为了兼容多个模板，所有模仿Spring 用List保存
            //在这写的代码中简化了，其实只有需要一个模板就可以搞定
            //只是为了仿真，所以还搞了个List
            this.viewResolvers.add(new GPViewResolver(templateRoot));
        }
    }
    
    private void initHandlerAdapters(GPApplicationContext context){
        //把一个request请求变成一个Handler，参数都是字符串的，自动配到handler中的形参
        //可想而知，他要拿到HandlerMapping 才能干活
        //就意味着，有几个HandlerMapping 就有几个HandlerAdapter
        for(GPHandlerMapping handlerMapping : this.handlerMappings){
            this.handlerAdapters.put(handlerMapping, new GPHandlerAdapter());
        }
    }
    
    private void initHandlerMappings(GPApplicationContext context){
        String[] beanNames = context.getBeanDefinitionNames();
        try{
            for(String beanName : beanName){
                Object controller = context.getBean(beanName);
                Class<?> clazz = controller.getClass();
                if(!clazz.isAnnotationPresent(GPController.calss)){
                    continue;
                }
                String baseUrl = "";
                //获取Controller的url配置
                if(clazz.isAnnotationPresent(GPRequestMapping.class)){
                    GPRequestMapping = requestMapping = 
                        clazz.getAnnotation(GPRequestMapping.class);
                    baseUrl = reqeustMapping.value();
                }
                
                //获取Method的url配置
                Method[] methods = clazz.getMethods();
                for(Method method : methods){
                    //没有加RequestMapping 注解的直接忽略
                    if(!method.isAnnotationPresent(GPRequetsMapping.calss)){continue;}
                    //映射url
                    GPRequestMapping reqeustMapping = 
                        method.getAnnotation(GPRequestMapping.class);
                    String regex = ("/" + baseUrl +"/" + reqeustMapping.value().replaceAll("\\*" , ".*")).replaceAll("/+" , "/");
                    Pattern pattern = Pattern.compile(regex);
                    this.handlerMappings.add(new GPHandlerMapping(pattern,controller,method));
                    log.info("Mapped " + regex + ","  + method);
                }
            }
        }catch(Exception e){...}
    }
    
}
```

GPHandlerMapping 请求映射

```java
import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class GPHandlerMapping{
    private Object controller; //保存方法对应的实例
    private Method method ; //保存映射的方法
    private Pattern pattern ;  //URL的正则匹配
    //。。构造
    //getter  setter ..
}
```

GPHandlerAdapter请求方法适配器

```java
import spring.framework.annotation.GPRequestParam;

import javax.servlet.http.HttpServletReqeust;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GPHandlerAdapter{
    public boolean supports(Object handler){return (handler instancefo GPHandlerMapping);}
    
    GPModelAndView handle(HttpServletRequest reqeust , HttpServletResponse response , Object handler) throws Exception {
        GPHandlerMapping handlerMapping = (GPHandlerMapping) handler;
        
        //把方法的形参列表和reqeust的参数列表所在顺序进行一一对应
        Map<String,Integer> paramIndexMapping = new HashMap<String,Integer>();
        
        //提取方法中的加了注解的参数
        //把方法上的注解拿到，得到的是一个二维数组
        //因为一个参数可以有多个注解，而一个方法又有多个参数
        Annotation[] pa handlerMapping.getMethod().getParameterAnnotations();
        for(int i= 0 ; i < pa.length ; i ++){
            for(Annotation a : pa[i]){
                if(a instanceof GPRequestParam){
                    String paramName = ((GPRequestParam) a ).value();
                    if(!"".equals(paramName.trim())){
                        paramIndexMapping.put(paramName  , i );
                    }
                }
            }
        }
        
        //提取方法中的reqeust和response参数
        Class<?>[] paramsTypes = handlerMapping.getMethod().getParameterTypes();
        for(int i = 0 ;i < paramsTypes.length ; i ++){
            Class<?> type = paramsTypes[i];
            if(type == HttpServletReqeust.class || 
              type == HttpServletResponse.class){
                paramIndexMapping.put(type.getName() , i);
            }
        }
        
        //获得方法的形参列表
        Map<String , String[]> params = request.getParamegerMap();
        
        //实参列表
        Object[] paramValues = new Object[paramsTypes.length];
        for(Map.Entry<String, String[]> param : params.entrySet()){
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]" , "").replaceAll("\\s" , ",");
            if(!paramIndexMapping.containsKey(param.getKey())){
                continue;
            }
            int index = paramIndexMapping.get(param.getKey());
            paramValues[index] = caseStringValue(value, paramsTypes[index]);
        }
        
        if(paramIndexMapping.containsKey(HtppServletRequest.class.getName())){
            int reqIndex = paramIndexMapping.get(HttpServletReqeust.class.getName());
            paramValues[reqIndex] = request;
        }
        if(paramIndexMapping.containsKey(HttpServletResponse.class.getName())){
            int respIndex = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex]  = response;
        }
        Object  result = handlerMapping.getMethod().invoke(handlerMapping.getController(),paramValues);
        if(result == null  || result.instanceof Void){return null;}
        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == GPModelAndView.class;
        if(isModelAndView){
            return (GPModelAndView) result;
        }
        return  null;
        
    }
    
    private Object  caseStringValue(String value , Class<?> paramsType){
        if(String.class == paramsType){
            return  value ;
        }
        if(Integer.class == paramsType){
            return Integer.valueOf(value);            
        }
        else if(Double.class == paramsType){
            return Double.valueOf(value);
        }
        else {
            if(value != null){
                return value ; 
            }
            return null;
        }
        //如果还有double或者其他类型，继续加if
        //这时候， 我们应该想到策略模式了
        
    }
}
```

GPModelAndView 页面数据封装

```java
import java.util.Map;
public class GPModelAndView{
    private String viewName;
    private Map<String,?> model;
}
```

GPViewResolver 视图解析器

```java
import java.util.Locale;
import java.io.File;
public class GPViewResolver{
    private final String DEFAULT_TEMPLATE_SUFFX = ".html";
    private Fiel templateRootDir;
    
    public GPViewResolver(String templateRoot){
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        templateRootDir = new File(templateRootPath);
    }
    public GPView resolveViewName(String viewName , Locale locale) throws Exception {
        if(null == viewName  || "".equals(viewName.trim())){return null;}
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+" , "/"));
        return new GPView(templateFile);
    }
}
```

GPView 自定义模板引擎

```java
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.RandomAccessFile;
import java.util.Map;
import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class GPView{
    private File viewFile;
    public GPView (File viewFile){ this.viewFile = viewFile; }
    
    public void render(Map<String,?> model , HttpServletRequest reqeust , HttpServletResponse reponse) throws Exception {
        StringBuffer sb = new StringBuffer();
        RandomAccessFile ra = new RandomAccessFile(this.viewFile , "r");
        String line = null;
        while(null != (line = ra.readLine())){
            line = new String (line.getBytes("ISO-8859-1") , "utf-8");
            Pattern pattern = Pattern.compile("￥\\{[^\\}]+\\}" , Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(line);
            while(matcher.find()){
                String paramName  = matcher.group();
                paramName = paramName.replaceAll("￥\\{|\\}" , "");
                Object paramValue = model.get(paramName);
                if(null == paramValue){continue;}
                line = matcher.replaceFirst(makeStringForRegExp(paramValue.toString()));
                matcher = pattern.matcher(line);
            }
            sb.append(line);
        }
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(sb.toString());
        
    }
    
    //处理特殊字符
    pubilc static String makeStringForRegExp(String str){
        return str.replace("\\" , "\\\\").replace("*", "\\*")
            .replace("+","\\+").replace("|","\\|")
            .replace("{" , "\\{").replace("}","\\}")
            .replace("(" , "\\(").replaceAll(")","\\)")
            .^ . $
            .[ . ]
            .? .,
        
    }
}
```

业务代码实现

IQueryService 查询业务接口定义

```java
pubilc interface IQueryService{
    public  String query(String name);
}
```

QueryService查询处理业务逻辑

```java
import java.text.SimpleDateFormat;
import java.util.Date;
import IQueryService;
import spring.framework.annotation.GPService;
import lombok.extern.slf4j.Slf4j;

@GPService
@Slf4j
public class QueryService implements IQueryService{
    public String query(String name){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        String json = "{name:\""+ name +"\",time:\""+ time +"}";
        return json;
    }
}
```

IModifyService 增删改查业务接口定义

```java
public interface IModifyService{
    public String add(String name , String addr );
    
    public String edit(Integer id , String name);
    
    public String remove(Integer id );
}
```

ModifyService 增删改查逻辑实现

```java
import  IModifyService;
import spring.framework.annotation.GPService;
@GPService
public class ModifyService implements IModifyService{
    public String add(String name , String addr){
        return "add ....";
    }
    
    public String edit(Integer id , String name ){
        return "edit .....";
    }
    
    public String remove(Integer id){
        return "modifyService id = "+id;
    }
}
```

MyAction 数据逻辑处理

```java
import java.io.IOException ; 
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletReqeust;
import javax.servlet.http.HttpServletResponse;

import IModifyService;
import IQueryService;
import spring.framework.annotation.GPAutowired;
import spring.framework.annotation.Controller;
import spring.framework.annotation.RequestMapping;
import spring.framework.annotation.RequestParam;
import spring.framework.webmvc.servlet.ModelAndView;

@Controller
@RequestMapping("/web")
public class MyAction{
    @Autowired
    IQueryService queryService;
    @Autowired
    IModifyService modifyService;
    
    @RequestMapping("/query.json")
    public ModelAndView query(HttpServletRequest req , HttpServletResponse resp , @ReqeustParam("name") String name){
        String result = queryService.query(name);
        return out(response , result);
    }
    
    .....add edit remove
        
        
    private ModelAndView out (HttpServletResponse resp , String str){
        resp.getWriter().write(str);
    }
}
```

PageAction 页面逻辑处理

```java
import java.util.HashMap;
import java.util.Map;

import IQueryService;
import.spring.framework.annotation.Autowired;
import spring.framework.annotation.Controller;
import spring.framework.annotation.RequestMapping;
import spring.framework.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PageAction{
 
    @Autowired
    IQueryService queryService;
    
    @RequestMapping("/first.html")
    public ModelAndView query(@RequestParam("teacher") String teacher){
        String result = questService.query(teacher);
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("teacher" , teacher);
        model.put("data" , result);
        model.put("token" , "123456");
        return new ModelAndView("first.html" , model);
    }
    
}
```



