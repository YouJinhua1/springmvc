package com.yjh.web.service.impl;

import com.yjh.anotation.Service;
import com.yjh.web.service.UserService;
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello(Integer id, String name) {
        return "hello " + id + " : " + name ;
    }
}
