package com.lx.oauth.srd.registrations.qq;

import com.lx.oauth.srd.config.TextHtmlHttpMessageConverter;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * 自定义实现OAuth2AccessTokenResponseClient接口.
 *
 * @date: 2022-03-04
 * @version: 1.0
 * @author: liufeifei02@beyondsoft.com
 */
public class QQOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private RestTemplate restTemplate;

    /**
     * 使用RestTemplate请求获取access_token，并对返回的结果执行自定义解析，最后构建成
     * OAuth2AccessTokenResponse对象返回
     * @param authorizationGrantRequest
     * @return
     */
    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
        OAuth2AuthorizationExchange oAuth2AuthorizationExchange = authorizationGrantRequest.getAuthorizationExchange();
        // 根据 API 文档获取请求 access_token 参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("client_id", clientRegistration.getClientId());
        params.set("client_secret", clientRegistration.getClientSecret());
        params.set("code", oAuth2AuthorizationExchange.getAuthorizationResponse().getCode());
        params.put("grant_type", Collections.singletonList("authorization_code"));
        String tmpTokenResponse = getRestTemplate().postForObject(clientRegistration.getProviderDetails().getTokenUri(),
                params, String.class);

        // 从 API 文档中可以获知解析 accessToken 的方式
        String[] items = tmpTokenResponse.split("&");
        String accessToken = items[0].substring(items[0].lastIndexOf("=") + 1);
        Long expiresIn = new Long(items[1].substring(items[1].lastIndexOf("=") + 1));

        Set<String> scopes = new LinkedHashSet<>(oAuth2AuthorizationExchange.getAuthorizationRequest().getScopes());
        Map<String, Object> additionalParameters = new LinkedHashMap<>();
        OAuth2AccessToken.TokenType accessTokenType = OAuth2AccessToken.TokenType.BEARER;

        return OAuth2AccessTokenResponse.withToken(accessToken)
                .tokenType(accessTokenType)
                .expiresIn(expiresIn)
                .scopes(scopes)
                .additionalParameters(additionalParameters)
                .build();
    }

    public RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new TextHtmlHttpMessageConverter());
        }
        return restTemplate;
    }
}