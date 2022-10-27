package com.springsecurity.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(path = "/")
    public String index(){
        return "index";
    }

    @GetMapping(path = "/home")
    public String home() {
        return "home";
    }

    @GetMapping(path = "/admin")
    public String admin() {
        return "admin";
    }
}
