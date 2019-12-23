package com.yjh.web.controller;

import com.yjh.anotation.Controller;
import com.yjh.anotation.RequestMapping;
import com.yjh.spring_2.View;
import com.yjh.web.service.LoginService;


import javax.annotation.Resource;

@Controller
public class LoginController {

    @Resource
    private LoginService loginService;

    //使用MyRequestMapping注解指明forward1方法的访问路径
    @RequestMapping("aaa")
    public View forward1(){
//执行完forward1方法之后返回的视图
        return new View("/WEB-INF/login.jsp");
    }
    //使用MyRequestMapping注解指明forward2方法的访问路径
    @RequestMapping("bbb")
    public View forward2(){
//执行完forward2方法之后返回的视图
        return new View("/bbb.jsp");
    }
}
