package com.chatchosim;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "redirect:/legacy/";
    }

    @GetMapping("/legacy")
    public String legacyNoSlash() {
        return "redirect:/legacy/";
    }

    @GetMapping("/legacy/")
    public String legacySlash() {
        return "forward:/legacy/index.html";
    }
}