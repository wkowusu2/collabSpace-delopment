package com.collabspace.collabspace.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String home() {
        return "<h1>Collabspace Home</h1>";
    }

    @GetMapping("/health")
    public String health() {
        return "<h1>Collabspace Working fine</h1>";
    }
}
