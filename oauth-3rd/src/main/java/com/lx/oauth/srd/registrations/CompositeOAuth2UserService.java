package com.lx.oauth.srd.registrations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 *
 * @date: 2022-03-04
 * @version: 1.0
 * @author: liufeifei02@beyondsoft.com
 */
public class CompositeOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private Map<String, OAuth2UserService> userServices;
    public static final String DEFAULT_USER_SERVICE_KEY = "default_key";

    public CompositeOAuth2UserService() {
        this.userServices = new HashMap<>();
        /*
            DefaultOAuth2UserService 是默认处理 OAuth 获取用户逻辑的
            OAuth2UserService 实现类
            将其预置到组合类 CompositeOAuth2UserService 中，从而默认支持 Google、Okta、GitHub 和 Facebook
         */
        this.userServices.put(DEFAULT_USER_SERVICE_KEY, new DefaultOAuth2UserService());
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService service = userServices.get(clientRegistration.getRegistrationId());
        if (service == null) {
            service = userServices.get(DEFAULT_USER_SERVICE_KEY);
        }
        return service.loadUser(userRequest);
    }

    public Map<String, OAuth2UserService> getUserServices() {
        return userServices;
    }
}