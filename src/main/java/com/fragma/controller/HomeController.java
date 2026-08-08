package com.fragma.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import com.fragma.entity.UserData;
import com.fragma.repository.UserDataRepository;



@Controller
public class HomeController {
	@Autowired
	private UserDataRepository repository;
	
	 @GetMapping("/")
	    public String home() {
	        return "index";
	    }

	@GetMapping("/profile")
	public String UserRegisterPage(@AuthenticationPrincipal OAuth2User user,Model model) {
	    String email=user.getAttribute("email");
	    
	    UserData userData = repository.findByEmail(email).orElse(null);
	    if(userData!=null) {
	    model.addAttribute("user",userData);
	    return "profile";
	   
	    
	}
	    else {
	    	 UserData user1 = new UserData();
	         user1.setName(user.getAttribute("name"));
	         user1.setEmail(email);
	         user1.setPicture(user.getAttribute("picture"));

	         model.addAttribute("user", user1);
	 	    	return "registration";
	    }
	}
	
	@PostMapping("/register123")
	public String register(@ModelAttribute UserData user, Model model) {

	    try {
	        repository.save(user);
	        return "redirect:/profile";

	    } catch (Exception e) {

	        model.addAttribute("user", user);
	        model.addAttribute("error", "Registration failed. Please try again.");

	        return "registration";
	    }
	}

	
}
