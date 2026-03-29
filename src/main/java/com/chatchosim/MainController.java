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

    @GetMapping("/legacy/index.html")
    public String legacyIndex() {
        return "redirect:/legacy/";
    }

    @GetMapping("/legacy/about")
    public String aboutNoSlash() {
        return "redirect:/legacy/about/";
    }

    @GetMapping("/legacy/about/")
    public String aboutSlash() {
        return "forward:/legacy/about/index.html";
    }

    @GetMapping("/legacy/about/index.html")
    public String aboutIndex() {
        return "redirect:/legacy/about/";
    }

    @GetMapping("/legacy/features")
    public String featuresNoSlash() {
        return "redirect:/legacy/features/";
    }

    @GetMapping("/legacy/features/")
    public String featuresSlash() {
        return "forward:/legacy/features/index.html";
    }

    @GetMapping("/legacy/features/index.html")
    public String featuresIndex() {
        return "redirect:/legacy/features/";
    }

    @GetMapping("/legacy/download")
    public String downloadNoSlash() {
        return "redirect:/legacy/download/";
    }

    @GetMapping("/legacy/download/")
    public String downloadSlash() {
        return "forward:/legacy/download/index.html";
    }

    @GetMapping("/legacy/download/index.html")
    public String downloadIndex() {
        return "redirect:/legacy/download/";
    }

    @GetMapping("/legacy/archive")
    public String archiveNoSlash() {
        return "redirect:/legacy/archive/";
    }

    @GetMapping("/legacy/archive/")
    public String archiveSlash() {
        return "forward:/legacy/archive/index.html";
    }

    @GetMapping("/legacy/archive/index.html")
    public String archiveIndex() {
        return "redirect:/legacy/archive/";
    }

    @GetMapping("/legacy/changelog")
    public String changelogNoSlash() {
        return "redirect:/legacy/changelog/";
    }

    @GetMapping("/legacy/changelog/")
    public String changelogSlash() {
        return "forward:/legacy/changelog/index.html";
    }

    @GetMapping("/legacy/changelog/index.html")
    public String changelogIndex() {
        return "redirect:/legacy/changelog/";
    }

    @GetMapping("/legacy/guide")
    public String guideNoSlash() {
        return "redirect:/legacy/guide/";
    }

    @GetMapping("/legacy/guide/")
    public String guideSlash() {
        return "forward:/legacy/guide/index.html";
    }

    @GetMapping("/legacy/guide/index.html")
    public String guideIndex() {
        return "redirect:/legacy/guide/";
    }

    @GetMapping("/legacy/contribute")
    public String contributeNoSlash() {
        return "redirect:/legacy/contribute/";
    }

    @GetMapping("/legacy/contribute/")
    public String contributeSlash() {
        return "forward:/legacy/contribute/index.html";
    }

    @GetMapping("/legacy/contribute/index.html")
    public String contributeIndex() {
        return "redirect:/legacy/contribute/";
    }
}