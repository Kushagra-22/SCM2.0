package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.Users;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("name", "Substring Technologies");
        model.addAttribute("githubRepo", "https://github.com/Kushagra-22");
        return "home";
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("isActive", false);
        return "about";
    }

    @GetMapping("/service")
    public String servicePage() {
        return "service";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {

        UserForm userForm = new UserForm();
        // userForm.setName("Kushagra");
        // userForm.setEmail("kushagrasharma0922@gmail.com");

        userForm.setAbout("lorem");
        // userForm.setPhoneNumber("9630037358");
        model.addAttribute("userForm", userForm);
        return "signup";
    }

    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String doRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBinidngResult,
            HttpSession session) {
        // Users user =
        // Users.builder().name(userForm.getName()).email(userForm.getEmail())
        // .password(userForm.getPassword()).about(userForm.getAbout()).phoneNumber(userForm.getPhoneNumber())
        // // .profilePic(
        // //
        // "https://instagram.fdel10-2.fna.fbcdn.net/v/t51.2885-19/441624793_2350422938480234_2911173017844761715_n.jpg?_nc_ht=instagram.fdel10-2.fna.fbcdn.net&_nc_cat=102&_nc_ohc=3EC7qun5nrwQ7kNvgGLlMNh&_nc_gid=02ed4c1944874343994aebd909e35083&edm=AP4sbd4BAAAA&ccb=7-5&oh=00_AYD878W3gej4W3kD0DvcUX9BAag47q3RtwfDCnUVRzOSfw&oe=6704001A&_nc_sid=7a9f4b")
        // .build();

        // Validation
        if(rBinidngResult.hasErrors()){
            return "signup";
        }
        Users user = new Users();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        Users savedUser = userService.saveUser(user);
        System.out.println(userForm);
        System.out.println("User Registered");
        // We have to fetch all form data and store that in database
        Message message = Message.builder().content("Registration successful").type(MessageType.red).build();

        session.setAttribute("message", message);
        return "redirect:/signup";
    }
}
