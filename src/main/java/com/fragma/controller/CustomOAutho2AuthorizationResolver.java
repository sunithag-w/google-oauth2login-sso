package com.fragma.controller;
import java.util.LinkedHashSet;
import java.util.Set;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomOAutho2AuthorizationResolver  implements OAuth2AuthorizationRequestResolver {

    private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

    public CustomOAutho2AuthorizationResolver(ClientRegistrationRepository clientRegistrationRepository) {
     this.defaultResolver =new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }

    @Override
    public OAuth2AuthorizationRequest resolve( HttpServletRequest request) {
        OAuth2AuthorizationRequest authorizationRequest =defaultResolver.resolve(request);
        return customize(request, authorizationRequest);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request,String clientRegistrationId) {
        OAuth2AuthorizationRequest authorizationRequest = defaultResolver.resolve(request,clientRegistrationId);
        return customize(request, authorizationRequest);
    }

    private OAuth2AuthorizationRequest customize( HttpServletRequest request, OAuth2AuthorizationRequest authorizationRequest) {
        if (authorizationRequest == null) {
            return null;
        }

        Object value =request.getSession().getAttribute("SELECTED_SCOPES");
        if (value == null) {
            return authorizationRequest;
        }      
        @SuppressWarnings("unchecked")              
        Set<String> scopes =new LinkedHashSet<>((java.util.List<String>) value);
        return OAuth2AuthorizationRequest.from(authorizationRequest).scopes(scopes) .build();
         
    }
}
