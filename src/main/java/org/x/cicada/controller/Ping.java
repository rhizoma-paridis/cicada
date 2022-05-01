package org.x.cicada.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class Ping {

    @GetMapping("index")
    public String index() {
        return "index";
    }

    @GetMapping("update")
    @Secured("ROLE_test")
    @PreAuthorize("hasAuthority('admin')")
    public String update() {
        return "update";
    }

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }
}
