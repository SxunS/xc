package com.example.xcframeworkfreemarker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.Map;

@Controller
@RequestMapping("/freemarker")
public class FreeMarkerController {

    @RequestMapping("/test1")
    public String freemarker(Map<String,Object> map){
        map.put("name","tom");
        return "test1";
    }
}
