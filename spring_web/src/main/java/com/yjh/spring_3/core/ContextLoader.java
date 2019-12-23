package com.yjh.spring_3.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;


/**
 * 加载配置文件的类
 */
public class ContextLoader {

    private static final Logger logger = LoggerFactory.getLogger(ContextLoader.class);

    public static void loadConfig(String location){
        if(location.startsWith("classpath:")){
            location=location.replace("classpath:","");
        }

        InputStream is = ContextLoader.class.getClassLoader().getResourceAsStream(location);
        try {
            ApplicationContext.properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("读取配置文件失败..........");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
