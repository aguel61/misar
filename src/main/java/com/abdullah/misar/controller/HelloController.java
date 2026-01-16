package com.abdullah.misar.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HelloController {
    

@GetMapping("/")
public String hello(){
    return "Hello Spring Boot";
}

@GetMapping("/api/test")
public String test() {
    return "API WORKS ;)";
}
}
