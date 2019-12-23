package com.yjh.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.yjh.anotation.Autowired;
import com.yjh.anotation.Controller;
import com.yjh.anotation.RequestMapping;
import com.yjh.anotation.RequestParam;

import com.yjh.web.service.UserService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    // http://localhost:8080/spring_web/user/hello.do?id=1&name=zhangsan
    @RequestMapping("/hello.do")
    public String hello(@RequestParam("id") Integer id,
                        @RequestParam("name") String name) throws IOException {
        return service.sayHello(id,name);
    }

    @RequestMapping("/hello2.do")
    public Object hello2(@RequestParam("id") Integer id,
                         @RequestParam("name") String name) throws IOException {
        Map<String,Object> datas = new HashMap<>();
        datas.put("id",id);
        datas.put("name",name);
        return JSONObject.toJSONString(datas);
    }
}
