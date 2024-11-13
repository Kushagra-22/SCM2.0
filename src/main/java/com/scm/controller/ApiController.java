package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.entities.contacts;
import com.scm.services.ContactService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contacts/{contactId}")
    public contacts getContacts(@PathVariable String contactId){
        // return contactService.getById();
        return contactService.getById(contactId);

    }
    // @DeleteMapping("/deleteContacts/{contactId}")
    // public void deleteContact(@PathVariable String contactId){
    //     contactService.delete(contactId);
    //     System.out.println(contactId +" deleted");
    // }
}
