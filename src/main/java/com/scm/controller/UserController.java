package com.scm.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.scm.entities.Users;
import com.scm.helper.Helper;
import com.scm.services.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger((UserController.class));

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedInUser(Model model, Authentication authentication) {
        System.out.println("Add Logged IN user");
        String username = Helper.getEmailLoggedInUser(authentication);
        Users user = userService.getUserByEmail(username);

        model.addAttribute("LoggedInUser", user);

    }

    // user dashboard
    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        try {
            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Create a request to the API
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.adviceslip.com/advice"))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // ObjectMapper mapper = new ObjectMapper();
            // JsonNode jsonResponse = mapper.readTree(response.body());
            // String advice = jsonResponse.path("slip").path("advice").asText();

            // Output the response body (JSON)
            String jsonResponse=response.body();
            String adviceKey = "\"advice\": \"";

            int startIndex = jsonResponse.indexOf(adviceKey) + adviceKey.length();
            int endIndex = jsonResponse.indexOf("\"", startIndex);
    
            // Extract the "advice" value
            String advice = jsonResponse.substring(startIndex, endIndex);
    
            model.addAttribute("response", advice);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String userProfile(Model model, Authentication authentication) {
        String username = Helper.getEmailLoggedInUser(authentication);
        Users user = userService.getUserByEmail(username);

        // logger.info("User logged in: "+user.getName());
        // logger.info(user.getEmail());
        model.addAttribute("LoggedInUser", user);

        return "user/profile";
    }
    @DeleteMapping("/products")
    public String getMethodName() {
        return "";
    }
    
    // user add contact
    // user view contact
    // user edit contact and delete contact
}
