package com.lx.oauth.srd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * .
 *
 * @date: 2022-03-04
 * @version: 1.0
 * @author: liufeifei02@beyondsoft.com
 */
@SpringBootApplication
@ComponentScan("com.lx.oauth.srd")
public class OAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuthApplication.class, args);
    }
}