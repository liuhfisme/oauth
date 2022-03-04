package com.lx.oauth.srd.config;

import com.lx.oauth.srd.registrations.CompositeOAuth2AccessTokenResponseClient;
import com.lx.oauth.srd.registrations.CompositeOAuth2UserService;
import com.lx.oauth.srd.registrations.qq.QQOAuth2AccessTokenResponseClient;
import com.lx.oauth.srd.registrations.qq.QQOAuth2UserService;
import com.lx.oauth.srd.registrations.qq.QQUserInfo;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * Spring Security 配置.<br/>
 *
 * Spring Security 5.0开始，在 HttpSecurity 中提供了用于配置 OAuth 客户端的策略
 * OAuth2Login（）方法。
 *
 * @date: 2022-03-04
 * @version: 1.0
 * @author: liufeifei02@beyondsoft.com
 */
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String QQ_REGISTRATION_ID = "qq";
    public static final String WECHAT_REGISTRATION_ID = "wechat";

    public static final String LOGIN_PAGE_PATH = "/login/oauth2";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(LOGIN_PAGE_PATH).permitAll()
                .anyRequest()
                .authenticated();

        http.oauth2Login()
                // 使用 CompositeOAuth2AccessTokenResponseClient
                .tokenEndpoint().accessTokenResponseClient(this.accessTokenResponseClient())
                .and()
                .userInfoEndpoint()
                // 使用 CompositeOAuth2UserService
                .customUserType(QQUserInfo.class, QQ_REGISTRATION_ID)
                .userService(oauth2UserService())
                // 可选，要保证与 redirect-uri-template 匹配
                .and()
                .redirectionEndpoint().baseUri("/register/social");

        // 自定义登录页
        http.oauth2Login().loginPage(LOGIN_PAGE_PATH);
    }

    private OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        CompositeOAuth2AccessTokenResponseClient client = new CompositeOAuth2AccessTokenResponseClient();
        // 加入 QQ 自定义 QQOAuth2AccessTokenResponseClient
        client.getClients().put(QQ_REGISTRATION_ID, new QQOAuth2AccessTokenResponseClient());
        return client;
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        CompositeOAuth2UserService service = new CompositeOAuth2UserService();
        // 加入 QQ 自定义 QQOAuth2UserService
        service.getUserServices().put(QQ_REGISTRATION_ID, new QQOAuth2UserService());
        return service;
    }
}