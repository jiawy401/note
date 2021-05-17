## Spring自动装配之依赖注入

### 依赖注入发生的时间

​	当SpringIoC容器完成了Bean定义资源的定位、载入和解析注册以后，IoC容器中已经管理类Bean定义的相关数据，但是此时IoC容器还没有对所管理的Bean进行依赖注入，依赖注入在以下两种情况发生：

1. 用户第一次调用getBean()方法时，IoC容器触发依赖注入。
2. 当用户在配置文件中将<bean>元素配置了lazy-init=false属性，即让容器在解析注册Bean定义时进行预实例化，触发依赖注入。



​	BeanFactory接口定义了Spring IoC容器的基本功能规范，是Spring IoC 容器所应遵守的最底层和最基本的编程规范。BeanFactory接口中定义了几个getBean()方法，就是用户向IoC容器索取管理的Bean的方法，我们通过分析其子类的具体实现，理解SpringIoC 容器在用户索取Bean时如何完成依赖注入。

![image-20210514100509593](process\image-20210514100509593.png)

在BeanFactory中我们可以看到getBean(String...) 方法，但它具体实现在AbstractBeanFactory中。

### 寻找获取Bean的入口

AbstractBeanFactory 的getBean() 相关方法的源码如下：

```java
//获取IoC容器中指定名称的Bean
@Override
public Object getBean(String name) throws BeansException{
    //doGetBean 才是真正向IoC容器获取被管理Bean的过程
    return doGetBean(name , null , null , false);
}

//获得IoC容器中指定名称和类型的Bean
@Override
public <T> T getBean(String name , @Nullable Class<T> requiredType) throws BeansException{
    //doGetBean 才是真正向IoC容器获取被管理Bean的过程
    return doGetBean(name , requiredType , null , false);
}

//获取IoC容器中指定名称和参数的Bean
@Override
public Object getBean(String name , Object ... args) throws BeansException{
    //doGetBean 才是真正向IoC容器获取被管理Bean的过程
    return doGetBean(name , null , args , false );
}
	
//获取IoC容器中指定名称、类型和参数的Bean
public <T> T getBean(String name,@Nullable Class<T> requiredType, @Nullable Object... args) throws BeansException {
    //doGetBean 才是真正向IoC容器获取被管理Bean的过程
        return this.doGetBean(name, requiredType, args, false);
}
//真正实现向IoC容器获取Bean的功能，也是触发依赖注入功能的地方
protected <T> T doGetBean(String name, Class<T> requiredType, final Object[] args, boolean typeCheckOnly) throws BeansException {
    
    //根据指定的名称获取被管理Bean的名称，剥离指定名称中对容器的相关依赖
    //如果指定的是别名，将别名转换为规范的Bean名称
        final String beanName = this.transformedBeanName(name);
        Object bean;
    
    //先从缓存中取是否已经有被创建过的单态类型的Bean
    //对于单例模式的Bean整个IoC容器中只创建一次，不需要重复创建
    Object sharedInstance = this.getSingleton(beanName);
    //IoC容器创建单例模式Bean实例对象
        if (sharedInstance != null && args == null) {
            if (this.logger.isDebugEnabled()) {
                //如果指定名称的Bean在容器中已有单例模式的Bean被创建
                //直接返回已创建的Bean
                if (this.isSingletonCurrentlyInCreation(beanName)) {
                    this.logger.debug("Returning eagerly cached instance of singleton bean '" + beanName + "' that is not fully initialized yet - a consequence of a circular reference");
                } else {
                    this.logger.debug("Returning cached instance of singleton bean '" + beanName + "'");
                }
            }
	//获取给定Bean的实例对象，主要是完成FactoryBean的相关处理
    //注意：BeanFactory是管理容器中Bean的工厂，而FactoryBean是
            //创建 创建对象的工厂Bean，两者之间有区别。
            bean = this.getObjectForBeanInstance(sharedInstance, name, beanName, (RootBeanDefinition)null);
        } else {
            //缓存没有正在创建的单例模式Bean
            //缓存中已经有已经创建的原型模式Bean
            //但是由于循环引用的问题导致实例化对象失败
            if (this.isPrototypeCurrentlyInCreation(beanName)) {
                throw new BeanCurrentlyInCreationException(beanName);
            }
		//对IoC容器中是否存在指定名称的BeanDefinition进行检查，首先检查是否
        //能在当前的BeanFactory中获取的所需要的Bean，如果不能则委托当前容器
        //的父级容器去查找，如果还是找不到则沿着容器的继承体系向父级容器查找。
            BeanFactory parentBeanFactory = this.getParentBeanFactory();
         //当前容器的父级容器存在，且当前容器中不存在指定名称的Bean。
            if (parentBeanFactory != null && !this.containsBeanDefinition(beanName)) {
                //解析指定Bean名称原始名称
                String nameToLookup = this.originalBeanName(name);
                if (args != null) {
                    //委派父级容器根据指定名称和显式的参数查找。
                    return parentBeanFactory.getBean(nameToLookup, args);
                }
				//委派父类容器根据指定名称和类型查找
                return parentBeanFactory.getBean(nameToLookup, requiredType);
            }
			//创建的Bean是否需要进行类型验证，一般不需要
            if (!typeCheckOnly) {
                //向容器标记指定的Bean已经被创建
                this.markBeanAsCreated(beanName);
            }

            try {
                final RootBeanDefinition mbd = this.getMergedLocalBeanDefinition(beanName);
                this.checkMergedBeanDefinition(mbd, beanName, args);
                String[] dependsOn = mbd.getDependsOn();
                String[] var11;
                if (dependsOn != null) {
                    var11 = dependsOn;
                    int var12 = dependsOn.length;

                    for(int var13 = 0; var13 < var12; ++var13) {
                        String dep = var11[var13];
                        if (this.isDependent(beanName, dep)) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                        }

                        this.registerDependentBean(dep, beanName);
                        this.getBean(dep);
                    }
                }

                if (mbd.isSingleton()) {
                    sharedInstance = this.getSingleton(beanName, new ObjectFactory<Object>() {
                        public Object getObject() throws BeansException {
                            try {
                                return AbstractBeanFactory.this.createBean(beanName, mbd, args);
                            } catch (BeansException var2) {
                                AbstractBeanFactory.this.destroySingleton(beanName);
                                throw var2;
                            }
                        }
                    });
                    bean = this.getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
                } else if (mbd.isPrototype()) {
                    var11 = null;

                    Object prototypeInstance;
                    try {
                        this.beforePrototypeCreation(beanName);
                        prototypeInstance = this.createBean(beanName, mbd, args);
                    } finally {
                        this.afterPrototypeCreation(beanName);
                    }

                    bean = this.getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
                } else {
                    String scopeName = mbd.getScope();
                    Scope scope = (Scope)this.scopes.get(scopeName);
                    if (scope == null) {
                        throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
                    }

                    try {
                        Object scopedInstance = scope.get(beanName, new ObjectFactory<Object>() {
                            public Object getObject() throws BeansException {
                                AbstractBeanFactory.this.beforePrototypeCreation(beanName);

                                Object var1;
                                try {
                                    var1 = AbstractBeanFactory.this.createBean(beanName, mbd, args);
                                } finally {
                                    AbstractBeanFactory.this.afterPrototypeCreation(beanName);
                                }

                                return var1;
                            }
                        });
                        bean = this.getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
                    } catch (IllegalStateException var21) {
                        throw new BeanCreationException(beanName, "Scope '" + scopeName + "' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton", var21);
                    }
                }
            } catch (BeansException var23) {
                this.cleanupAfterBeanCreationFailure(beanName);
                throw var23;
            }
        }

        if (requiredType != null && bean != null && !requiredType.isInstance(bean)) {
            try {
                return this.getTypeConverter().convertIfNecessary(bean, requiredType);
            } catch (TypeMismatchException var22) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Failed to convert bean '" + name + "' to required type '" + ClassUtils.getQualifiedName(requiredType) + "'", var22);
                }

                throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
            }
        } else {
            return bean;
        }
    }

```

