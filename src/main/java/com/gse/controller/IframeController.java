package com.gse.controller;

import com.gse.service.OttService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iframe")
public class IframeController {

    private final OttService ottService;

    public IframeController(OttService ottService) {
        this.ottService = ottService;
    }

    @GetMapping("/bootstrap")
    public String bootstrap(@RequestParam String ott) {

        String data = ottService.consume(ott);

        if (data == null) {
            return "Invalid or expired OTT";
        }

        return "SESSION CREATED - Redirecting to /app";
    }
}