package com.yjh;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ViewResolver;

@Controller
public class LoginController {


    @RequestMapping("/index")
    public String index() {
        System.out.println("前往首页！");
        return "index";
    }


}
