package com.scm.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;



public class Helper {
    // Logger logger=LoggerFactory.getLogger(this.getClass());
    public static  String getEmailLoggedInUser(Authentication authentication) {


        if (authentication instanceof OAuth2AuthenticationToken) {
            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            String clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            var oAuth2User = (OAuth2User) authentication.getPrincipal();
            String username = "";

            if (clientId.equalsIgnoreCase("google")) {
                username = oAuth2User.getAttribute("email").toString();
                System.out.println("Getting email from google client");
            }
            return username;
        } else {
            return authentication.getName();

        }

    }
}