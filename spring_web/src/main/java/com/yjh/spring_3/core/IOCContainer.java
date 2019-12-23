package com.yjh.spring_3.core;

import com.yjh.anotation.Autowired;
import com.yjh.anotation.Controller;
import com.yjh.anotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IOCContainer {
    private static final Logger logger = LoggerFactory.getLogger(ContextLoader.class);

    // 定义iocMap容器
    private static Map<String, Object> iocMap = new HashMap<>();

    /**
     * 获取IOC容器
     *
     * @return
     */
    public static Map<String, Object> getContainer() {
        return iocMap;
    }

    /**
     * 获取bean实例
     *
     * @return
     */
    public static Object getBean(String beanName) {
        return iocMap.get(beanName);
    }

    /**
     * 容器初始化化
     */
    public static void init() {
        // 从配置文件中获取要扫描的包路径
        String scanPackage = ApplicationContext.properties.getProperty(ConfigurationConstant.AUTOSCAN_PACKAGE);
        // 扫描包路径下的所有class文件
        Set<Class<?>> clazzList = ScanClassUtils.getClasses(scanPackage);
        if (clazzList.isEmpty())
            return;

        try {
            for (Class<?> clazz : clazzList) {
                // 判断该类上是否添加了Controller注解
                if (clazz.isAnnotationPresent(Controller.class)) {
                    // 将名字首字母小写
                    String beanName = lowerFirst(clazz.getSimpleName());
                    // 创建该class文件的实例，并放入ioc容器中
                    iocMap.put(beanName, clazz.getDeclaredConstructor().newInstance());
                }
                // 判断该类上是否添加了Service注解
                if (clazz.isAnnotationPresent(Service.class)) {
                    // 获取标注在该类上的service注解对象
                    Service service = clazz.getAnnotation(Service.class);
                    // 获取注解上的默认值
                    String beanName = service.value();
                    // 判断默认值是否为空
                    if (beanName.equals("")) {
                        beanName = lowerFirst(clazz.getSimpleName());
                    }
                    // 创建对应的实例
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    // 放入容器中
                    iocMap.put(beanName, instance);
                    // 获取该类所实现的所有接口的class
                    Class<?>[] interfaces = clazz.getInterfaces();
                    // 遍历集合
                    for (Class<?> c : interfaces) {
                        /**
                         * 为了使如下方式：
                         *      @Service
                         *      private UserService userService
                         * 也能找到UserServiceImpl，
                         * 所以将该类所实现的接口的名称与该实例也关联起来。
                         */
                        iocMap.put(lowerFirst(c.getName()), instance);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将字符串的首字母转为小写
     * @param value
     * @return string
     */
    private static String lowerFirst(String value) {
        return Character.isLowerCase(value.charAt(0)) ? value : (new StringBuilder()).append(Character.toLowerCase(value.charAt(0))).append(value.substring(1)).toString();
    }

    /**
     * 依赖注入
     */
    private static void autowired(){
        if (iocMap.isEmpty())
            return ;
        for (Map.Entry<String,Object> entry: iocMap.entrySet()) {
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field: fields) {
                if (!field.isAnnotationPresent(Autowired.class)||!field.isAnnotationPresent(Resource.class))
                    continue;
                String beanName="";
                if(field.isAnnotationPresent(Autowired.class)){
                    Autowired autowired=field.getAnnotation(Autowired.class);
                    beanName = autowired.value();
                }
                if (beanName.equals("")) {
                    beanName = lowerFirst(field.getType().getName());
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(),iocMap.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    logger.error("依赖注入："+field.getName()+"失败!");
                }
            }
        }
    }


}
