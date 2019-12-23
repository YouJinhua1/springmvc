package com.yjh.spring_2;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储方法的访问路径
 */
public class RequestMap {
    /**
     * requesetMap:用于存储方法的访问路径和类
     */
    private static Map<String, Class<?>> requesetMap = new HashMap<String, Class<?>>();

    /**
     * 根据url获取对应的处理器Controller的类名
     * @param url
     * @return  Class<?> className
     */
    public static Class<?> getClassName(String url) {
        return requesetMap.get(url);
    }

    /**
     * 向requestMap中添加映射关系
     * @param url
     * @param className
     */
    public static void put(String url, Class<?> className) {
        requesetMap.put(url, className);
    }

    /**
     * 获取requestMap
     * @return requestMap
     */
    public static Map<String, Class<?>> getRequesetMap() {
        return requesetMap;
    }
}
