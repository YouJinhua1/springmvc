package com.yjh.spring_2;




import com.yjh.anotation.Controller;
import com.yjh.anotation.RequestMapping;
import com.yjh.anotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Description: AnnotationHandleServlet作为自定义注解的核心处理器
 * 以及负责调用目标业务方法处理用户请求<p>
 */
public class AnnotationHandleServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AnnotationHandleServlet.class);

    private String pareRequestURI(HttpServletRequest request) {
        String path = request.getContextPath() + "/";
        String requestUri = request.getRequestURI();
        String midUrl = requestUri.replaceFirst(path, "");
        String lasturl = midUrl.substring(0, midUrl.lastIndexOf("."));
        return lasturl;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.excute(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.excute(request, response);
    }

    private void excute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //将当前线程中HttpServletRequest对象存储到ThreadLocal中，以便在Controller类中使用
        WebContext.requestHodler.set(request);
        //将当前线程中HttpServletResponse对象存储到ThreadLocal中，以便在Controller类中使用
        WebContext.responseHodler.set(response);
        //解析url
        String lasturl = pareRequestURI(request);
        //获取要使用的类
        Class<?> clazz = RequestMap.getRequesetMap().get(lasturl);
        //创建类的实例
        Object classInstance = BeanUtils.instanceClass(clazz);

//        String beanName=clazz.getSimpleName();
//        beanName=Character.isLowerCase(beanName.charAt(0)) ? beanName : (new StringBuilder()).append(Character.toLowerCase(beanName.charAt(0))).append(beanName.substring(1)).toString();
//        Object classInstance = IocContainer.getBean(beanName);
        //获取类中定义的方法
        Method[] methods = BeanUtils.findDeclaredMethods(clazz);
        Method method = null;
        for (Method m : methods) {//循环方法，找匹配的方法进行执行
            if (m.isAnnotationPresent(com.yjh.anotation.RequestMapping.class)) {
                String anoPath = m.getAnnotation(com.yjh.anotation.RequestMapping.class).value();
                if (anoPath != null && !"".equals(anoPath.trim()) && lasturl.equals(anoPath.trim())) {
                    //找到要执行的目标方法
                    method = m;
                    break;
                }
            }
        }
        try {
            if (method != null) {
                //执行目标方法处理用户请求
                Object retObject = method.invoke(classInstance);
                //如果方法有返回值，那么就表示用户需要返回视图
                if (retObject != null) {
                    View view = (View) retObject;
                    //判断要使用的跳转方式
                    if (view.getDispathAction().equals(DispatchActionConstant.FORWARD)) {
                        //使用转发方式
                        request.getRequestDispatcher(view.getUrl()).forward(request, response);
                    } else if (view.getDispathAction().equals(DispatchActionConstant.REDIRECT)) {
                        //使用重定向方式
                        response.sendRedirect(request.getContextPath() + view.getUrl());
                    } else {
                        request.getRequestDispatcher(view.getUrl()).forward(request, response);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        /**
         * 重写了Servlet的init方法后一定要记得调用父类的init方法，
         * 否则在service/doGet/doPost方法中使用getServletContext()方法获取ServletContext对象时
         * 就会出现java.lang.NullPointerException异常
         */
        super.init(config);

        logger.info("---初始化开始---");
        try{
            int a=4/0;
        }catch(Exception e){
            logger.error(e.toString());
        }
        System.out.println("---初始化开始---");
        //获取web.xml中配置的要扫描的包
        String basePackage = config.getInitParameter("basePackage");
        //如果配置了多个包，例如:<param-value>me.gacl.web.controller,me.gacl.web.UI</param-value>
        if (basePackage.indexOf(",") > 0) {
            //按逗号进行分隔
            String[] packageNameArr = basePackage.split(",");
            for (String packageName : packageNameArr) {
                initConfig(packageName);
            }
        } else {
            initConfig(basePackage);
        }
        logger.warn("----初始化结束---");
        System.out.println("----初始化结束---");
    }

    /**
     * @param clazz
     * @Method: initRequestMapingMap
     * @Description:添加使用了Controller注解的Class到RequestMapingMap中
     * @Anthor:孤傲苍狼
     */
    private void initRequestMapingMap(Class<?> clazz) {
        Method[] methods = BeanUtils.findDeclaredMethods(clazz);
        for (Method m : methods) {//循环方法，找匹配的方法进行执行
            if (m.isAnnotationPresent(RequestMapping.class)) {
                String anoPath = m.getAnnotation(RequestMapping.class).value();
                if (anoPath != null && !"".equals(anoPath.trim())) {
                    if (RequestMap.getRequesetMap().containsKey(anoPath)) {
                        throw new RuntimeException("RequestMapping映射的地址不允许重复!");
                    }
                    RequestMap.put(anoPath, clazz);
                }
            }
        }
    }

    /**
     * @param packageName
     * @Method: initConfig
     * @Description:添加使用了Controller注解的Class到RequestMapingMap中
     * @Anthor:孤傲苍狼
     */
    private void initConfig(String packageName) {
        Map<String, Class<?>> classMap = ScanClassUtils.getClassMap(packageName);
        for (String key : classMap.keySet()) {
            Class<?> clazz = classMap.get(key);
            if (clazz.isAnnotationPresent(Controller.class)) {
                initRequestMapingMap(clazz);
            }
            // 创建所有的bean
            if (clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class)) {
                createBeans(clazz);
            }
            //
        }
    }

    /**
     * 创建IOC容器中Bean的实例
     */
    private void createBeans(Class<?> clazz) {
        Object classInstance = BeanUtils.instanceClass(clazz);
        String beanName = clazz.getSimpleName();
        // 类名首字母小写
        beanName=Character.isLowerCase(beanName.charAt(0))?beanName:(new StringBuilder()).append(Character.toLowerCase(beanName.charAt(0))).append(beanName.substring(1)).toString();

       // IocContainer.put(beanName, classInstance);
    }

}
