package com.fragma.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.fragma.entity.UserData;
import com.fragma.repository.UserDataRepository;



@Controller
public class HomeController {
	@Autowired
	private UserDataRepository repository;
	@Autowired
	private OAuth2AuthorizedClientService authorizedClientService;
	
	 @GetMapping("/")
	    public String home() {
	        return "index";
	    }
	 
	
	@GetMapping("/profile")
	public String UserRegisterPage(@AuthenticationPrincipal OAuth2User user, OAuth2AuthenticationToken authentication, Model model) {
		
		  String registrationId = authentication.getAuthorizedClientRegistrationId();
		    String email = null;
		    String name = null;
		    String picture = null;
		    if (registrationId.equals("google")) {

		        email = user.getAttribute("email");
		        name = user.getAttribute("name");
		        picture = user.getAttribute("picture");
		    } 
		    else if (registrationId.equals("github")) {
		        name = user.getAttribute("name");
		        picture = user.getAttribute("avatar_url");
		        email = user.getAttribute("email");
		       
		    }
		System.out.println("Picture : " + picture);
		UserData userData = (email != null) ? repository.findByEmail(email).orElse(null) : null;
		if (userData != null) {
			model.addAttribute("user", userData);
			return "profile";
		} else {
			UserData user1 = new UserData();
			System.out.println("email : " + email);
			user1.setName(name);
			user1.setEmail(email);
			user1.setPicture(picture);

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
