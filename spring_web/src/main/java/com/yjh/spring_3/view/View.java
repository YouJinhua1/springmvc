package com.yjh.spring_3.view;

import com.yjh.spring_3.core.ConfigurationConstant;

public class View {
    private String path;

    public View(String path) {
        this.path+= ConfigurationConstant.VIEW_PREFIX;
        this.path+= path;
        this.path+=ConfigurationConstant.VIEW_SUFFIX;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path+=ConfigurationConstant.VIEW_PREFIX;
        this.path+= path;
        this.path+=ConfigurationConstant.VIEW_SUFFIX;
    }
}
