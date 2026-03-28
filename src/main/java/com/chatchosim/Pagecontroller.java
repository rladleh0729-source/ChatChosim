package com.chatchosim;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Pagecontroller {

    @GetMapping("/")
    public String chatPage() {
        return "chat";
    }

    @GetMapping("/legacy")
    public String legacyRedirect() {
        return "redirect:/legacy/index.html";
    }
}