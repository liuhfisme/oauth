package com.lx.oauth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * .
 *
 * @date: 2022-03-07
 * @version: 1.0
 * @author: liufeifei02@beyondsoft.com
 */
@Profile("minimal")
@SpringBootApplication
@EnableAuthorizationServer
public class OAuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuthServerApplication.class, args);
    }
}