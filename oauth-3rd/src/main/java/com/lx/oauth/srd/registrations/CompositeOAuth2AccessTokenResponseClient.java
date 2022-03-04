package com.lx.oauth.srd.registrations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.endpoint.NimbusAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 多个OAuth服务提供商共存支持.<br/>
 *
 * 前面我们通过自定义实现QQOAuth2AccessTokenResponseClient和QQOAuth2UserService来支持
 * QQ登录，但如果直接使用它们分别代替默认的NimbusAuthorizationCodeTokenResponseClient和
 * DefaultOAuth2UserService，将会导致GitHub等标准OAuth服务无法正常使用。为了让多个OAuth服务
 * 可以并存，建议使用组合模式<br/>
 *
 * OAuth2AccessTokenResponseClient 的组合类，使用了 Composite Pattern（组合模式）。
 * 除支持Google、Okta、GitHub和Facebook外，还支持 QQ、微信等多种认证服务，
 * 可根据 registrationId 选择相应的 OAuth2AccessTokenResponseClient
 *
 * @date: 2022-03-04
 * @version: 1.0
 * @author: liufeifei02@beyondsoft.com
 */
public class CompositeOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private Map<String, OAuth2AccessTokenResponseClient> clients;

    public static final String DEFAULT_CLIENT_KEY = "default_key";

    public CompositeOAuth2AccessTokenResponseClient() {
        this.clients = new HashMap<>();
        /*
            spring-security-oauth2-client 默认的 OAuth2AccessTokenResponse
            Client 实现类是 NimbusAuthorizationCodeTokenResponseClient
            将其预置到组合类 CompositeOAuth2AccessTokenResponseClient 中，
            使其默认支持 Google、Okta、GitHub 和 Facebook
         */
        this.clients.put(DEFAULT_CLIENT_KEY, new NimbusAuthorizationCodeTokenResponseClient());
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
        OAuth2AccessTokenResponseClient client = clients.get(clientRegistration.getRegistrationId());
        if (client == null) {
            client = clients.get(DEFAULT_CLIENT_KEY);
        }
        return client.getTokenResponse(authorizationGrantRequest);
    }

    public Map<String, OAuth2AccessTokenResponseClient> getClients() {
        return clients;
    }
}