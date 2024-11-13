package com.scm.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Users;
import com.scm.entities.contacts;
import com.scm.forms.ContactForm;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @RequestMapping("/add")
    public String addContact(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);

        return "user/addContact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String loadContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult,
            Authentication authentication, HttpSession httpSession) {
        String username = Helper.getEmailLoggedInUser(authentication);
        Users user = userService.getUserByEmail(username);

        String fileName = UUID.randomUUID().toString();
        String fileURL = imageService.uploadImage(contactForm.getContactImage(), fileName);

        if (bindingResult.hasErrors()) {
            httpSession.setAttribute("message",
                    Message.builder()
                            .content("Correct your errors")
                            .type(MessageType.red)
                            .build());
            return "user/addContact";
        }
        logger.info("file information:{}", contactForm.getContactImage().getOriginalFilename());
        contacts contact = new contacts();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setFavourite(contactForm.isFavourite());
        contact.setUser(user);
        contact.setLinkedinLink(contactForm.getLinkedinLink());
        contact.setDescription(" ");
        contact.setCloudinaryImagePublicId(fileName);
        contact.setProfilePic(fileURL);

        contactService.save(contact);

        // httpSession.setAttribute("message", Message.builder()
        // .content("You have successfully added contact")
        // .type(MessageType.green)
        // .build());
        System.out.println(contactForm);
        return "redirect:/user/contacts/add";
    }

    @RequestMapping
    public String viewContact(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "7") int size,
            Model model,
            Authentication authentication) {
        // load all contact on contacts page
        String username = Helper.getEmailLoggedInUser(authentication);
        Users user = userService.getUserByEmail(username);

        Page<contacts> contactDetaiList = contactService.getByUser(user, page, size);

        // List<contacts> contactDetaiList = contactService.getAllContacts();

        // for (contacts contacts : contactDetaiList) {
        // System.out.println("Contact details.." + contacts.getName());
        // }

        model.addAttribute("Contacts", contactDetaiList);
        return "user/contacts";
    }

    // Search handler
    @RequestMapping("/search")
    public String searchContact(
            @RequestParam("field") String field,
            @RequestParam("keyword") String keyword,
            Model model,
            Authentication authentication

    ) {

        Page<contacts> contactDetaiList;
        //
        var user = userService.getUserByEmail(Helper.getEmailLoggedInUser(authentication));

        if (field.equalsIgnoreCase("name")) {
            contactDetaiList = contactService.searchByName(user, keyword, 0, 7);
            System.out.println("Entered into name");

        } else if (field.equalsIgnoreCase("email")) {
            contactDetaiList = contactService.searchByEmail(user, keyword, 0, 7);
        } else {
            contactDetaiList = contactService.searchByPhone(user, keyword, 0, 7);
        }
        model.addAttribute("Contacts", contactDetaiList);
        return "user/search";

    }

    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId, HttpSession session) {
        contactService.delete(contactId);
        session.setAttribute("message",
                Message.builder()
                        .content("Contact is deleted successfully !!")
                        .type(MessageType.green)
                        .build());
        System.out.println("Users leted");
        return "redirect:/user/contacts";
    }

    @RequestMapping("/view/{contactId}")
    public String viewContact(@PathVariable String contactId, Model model) {
        logger.info("User if for update: " + contactId);

        contacts contactInfo = contactService.getById(contactId);

        ContactForm contactForm = new ContactForm();
        contactForm.setName(contactInfo.getName());
        contactForm.setEmail(contactInfo.getEmail());
        contactForm.setAddress(contactInfo.getAddress());
        contactForm.setFavourite(contactInfo.isFavourite());
        contactForm.setLinkedinLink(contactInfo.getLinkedinLink());
        contactForm.setPhoneNumber(contactInfo.getPhoneNumber());
        contactForm.setPicture(contactInfo.getProfilePic());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);
        // contactForm.setContactImage(contactInfo.getCloudinaryImagePublicId());
        return "/user/view";
        // return "redirect:/user/contacts";

    }

    @RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
    public String updateContact(
            @PathVariable String contactId,
            @ModelAttribute ContactForm contactForm,
            Model model) {
                contacts c=new contacts();
                c.setId(contactId);
                c.setAddress(contactForm.getAddress());
                c.setName(contactForm.getName());
                c.setPhoneNumber(contactForm.getPhoneNumber());
                c.setEmail(contactForm.getEmail());
                c.setFavourite(contactForm.isFavourite());
                c.setLinkedinLink(contactForm.getLinkedinLink());
                contactService.update(c);
        return "redirect:/user/contacts";
    }

}
