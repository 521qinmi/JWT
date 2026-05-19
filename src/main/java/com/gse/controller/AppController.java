package com.gse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @GetMapping("/app")
    public String app() {
        return "GSE Staffing Plan UI Loaded";
    }
}