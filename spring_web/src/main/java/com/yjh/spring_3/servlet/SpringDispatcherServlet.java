package com.yjh.spring_3.servlet;

import com.yjh.spring_3.request.RequestMappingResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SpringDispatcherServlet extends DispatcherServlet {

    @Override
    protected void excute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.excute(request, response);
        RequestMappingResolver.handlerRequest();
    }
}
