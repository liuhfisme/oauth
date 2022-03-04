package com.lx.oauth.srd.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * .
 *
 * @date: 2022-03-04
 * @version: 1.0
 * @author: liufeifei02@beyondsoft.com
 */
@RestController
public class SimpleController {
    @GetMapping("/hello")
    public String hello(Principal principal) {
        return "hello. " + principal.getName();
    }
}