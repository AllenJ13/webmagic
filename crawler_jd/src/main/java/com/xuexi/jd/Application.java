package com.xuexi.jd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * springboot的引导类
 */
@SpringBootApplication
//使用定时任务
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        /**
         * 第一个参数是引导类的class
         * 第二个参数是args
         */
        SpringApplication.run(Application.class,args);
    }
}
