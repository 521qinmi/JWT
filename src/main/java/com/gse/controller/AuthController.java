package com.gse.controller;

import com.gse.service.JwtService;
import com.gse.service.OttService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final OttService ottService;

    public AuthController(JwtService jwtService, OttService ottService) {
        this.jwtService = jwtService;
        this.ottService = ottService;
    }

    @PostMapping("/bootstrap")
    public Map<String, Object> bootstrap(@RequestBody Map<String, String> body) {

        String jwt = body.get("jwt");

        jwtService.validate(jwt);

        String ott = ottService.generateOtt("user-session");

        Map<String, Object> res = new HashMap<>();
        res.put("iframeUrl", "https://gseXXXX.com/iframe/bootstrap?ott=" + ott);

        return res;
    }
}