package com.scm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.Users;
import com.scm.helper.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {

    @Autowired
    UserService userService;

  
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ModelAttribute
    public void addLoggedInUser(Model model, Authentication authentication) {
        if(authentication==null){
            return ; 
        }
        // System.out.println("Add Logged IN user");

        String username = Helper.getEmailLoggedInUser(authentication);
        logger.info("User logged in: {}", authentication.getName());
        Users user = userService.getUserByEmail(username);
        if (user == null) {
            model.addAttribute("LoggedInUser", null);
        } else {
            // System.out.println(user.getName());
            model.addAttribute("LoggedInUser", user);
        }
    }
}
