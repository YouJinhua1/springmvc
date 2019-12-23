package com.yjh.spring_3.core;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Properties;

/**
 * web上下文，通过上下文能够获取：
 *      request，response，session，servletContext
 */
public class ApplicationContext {

    // 保存全局配置文件中的配置信息
    public static Properties properties=new Properties();
    public static ThreadLocal<HttpServletRequest> requestHodler = new ThreadLocal<HttpServletRequest>();
    public static ThreadLocal<HttpServletResponse> responseHodler = new ThreadLocal<HttpServletResponse>();
    public static HttpServletRequest getRequest(){
        return requestHodler.get();
    }
    public static HttpSession getSession(){
        return requestHodler.get().getSession();
    }
    public static ServletContext getServletContext(){
        return requestHodler.get().getSession().getServletContext();
    }
    public static HttpServletResponse getResponse(){
        return responseHodler.get();
    }
    public static Properties getpropConfig(){
        return properties;
    }
}
