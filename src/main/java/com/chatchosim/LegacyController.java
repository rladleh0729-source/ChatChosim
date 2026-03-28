package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LegacyController {

    @GetMapping("/legacy")
    public String legacy() {
        return "forward:/legacy/index.html";
    }

    @GetMapping("/legacy/")
    public String legacySlash() {
        return "forward:/legacy/index.html";
    }
}