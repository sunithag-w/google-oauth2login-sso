package com.fragma.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;

@Controller
public class ScopeController {

    @GetMapping("/select-scope")
    public String scopePage( @RequestParam String provider, HttpSession session, Model model) {
     session.setAttribute("OAUTH_PROVIDER", provider);
     model.addAttribute("provider", provider);
     return "scope";
    }

    @PostMapping("/select-scope")
    public String selectScope( @RequestParam(required = false) List<String> scope, HttpSession session) {
        if (scope == null) {
            scope = new ArrayList<>();
        }
        String provider =(String) session.getAttribute("OAUTH_PROVIDER");

        if (provider == null) {
            return "redirect:/";
        }

        if ("google".equalsIgnoreCase(provider)&& !scope.contains("openid")) {
            scope.add("openid");
        }
        session.setAttribute("SELECTED_SCOPES", scope);
        return "redirect:/oauth2/authorization/" + provider;
    }
}