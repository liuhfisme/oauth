package com.lx.oauth.srd.config;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * RestTemplate解析模板.
 *
 * @date: 2022-03-04
 * @version: 1.0
 * @author: liufeifei02@beyondsoft.com
 */
public class JacksonFromTextHtmlHttpMessageConverter extends MappingJackson2HttpMessageConverter {
    /**
     * 添加对 text/html 的支持
     */
    public JacksonFromTextHtmlHttpMessageConverter() {
        List mediaTypes = new ArrayList();
        mediaTypes.add(MediaType.TEXT_HTML);
        setSupportedMediaTypes(mediaTypes);
    }
}