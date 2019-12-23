package com.yjh.spring_3.request;

import java.lang.reflect.Method;
/**
 * handler请求处理类
 */
public class RequestHandler {
    private Object controller;
    private Method method;
    private String url;

    public RequestHandler(Object controller, Method method, String url) {
        this.controller = controller;
        this.method = method;
        this.url = url;
    }

    public Object getController() {
        return controller;
    }
    public Method getMethod() {
        return method;
    }
    public String getUrl() {
        return url;
    }
}
