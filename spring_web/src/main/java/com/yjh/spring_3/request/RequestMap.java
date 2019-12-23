package com.yjh.spring_3.request;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RequestMap{
    /**
     * requesetMap:用于存储方法的访问路径和类
     */
    private static Map<String, RequestHandler> requesetMap = new HashMap<String, RequestHandler>();

    /**
     * 根据url获取对应的处理器handler
     * @param url
     * @return  Handler
     */
    public static RequestHandler getHandler(String url) {
        return requesetMap.get(url);
    }

    /**
     * 向requestMap中添加对应的Handler
     * @param url
     * @param controller
     * @param method
     */
    public static void put(String url, Object controller,Method method) {
        requesetMap.put(url, new RequestHandler(controller,method,url));
    }

    /**
     * 获取请求Mapping
     * @return requestMap
     */
    public static Map<String, RequestHandler> getMapping() {
        return requesetMap;
    }


}
