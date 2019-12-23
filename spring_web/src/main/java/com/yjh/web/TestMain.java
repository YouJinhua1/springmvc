package com.yjh;

import com.yjh.spring_2.BeanUtils;
import com.yjh.web.entity.User;


import java.lang.reflect.Method;

public class TestMain {
    public static void main(String[] args) throws Exception {
        User user = BeanUtils.instanceClass(User.class.getConstructor(String.class, String.class, Integer.class), "zhangsan", "男", 22);

        User user1 = BeanUtils.instanceClass(User.class);

        Method method =BeanUtils.findDeclaredMethod(User.class, "setUsername", new Class[]{String.class});

        System.out.println(method.getName());
        System.out.println(user);
        System.out.println(user1);


    }
}
