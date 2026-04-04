package com.chatchosim.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {

    @GetMapping("/chosim_admin")
    public String adminNoSlash() {
        return "redirect:/chosim_admin/";
    }

    @GetMapping("/chosim_admin/")
    public String adminHome() {
        return "forward:/chosim_admin/index.html";
    }
}