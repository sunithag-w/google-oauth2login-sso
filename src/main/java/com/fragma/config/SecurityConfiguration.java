package com.fragma.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

import com.fragma.controller.CustomOAutho2AuthorizationResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
    @Bean
    public CustomOAutho2AuthorizationResolver authorizationRequestResolver( ClientRegistrationRepository clientRegistrationRepository) {
        return new CustomOAutho2AuthorizationResolver( clientRegistrationRepository);          
    }

	   @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf(csrf->csrf.disable())
	            
	            .authorizeHttpRequests(auth -> auth
	            	.requestMatchers("/",  "/select-scope", "/start-google-login","/images/**").permitAll()	
	                       	
	                .anyRequest().authenticated())
	            
	            .oauth2Login(oauth -> oauth
	                    .defaultSuccessUrl("/profile", true))
	                
	                .logout(logout -> logout
	                    .logoutUrl("/logout")
	                    .logoutSuccessUrl("/")
	                    .invalidateHttpSession(true)
	                    .clearAuthentication(true)
	                    .deleteCookies("JSESSIONID")
	                    .permitAll()
	                );
	        return http.build();
	    }
}
