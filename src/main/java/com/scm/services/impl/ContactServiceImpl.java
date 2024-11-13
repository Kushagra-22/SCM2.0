package com.scm.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.scm.entities.Users;
import com.scm.entities.contacts;
import com.scm.repo.ContactRepo;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public contacts save(contacts contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepo.save(contact);
    }

    @Override
    public contacts update(contacts contact) {
        var contactUp=contactRepo.findById(contact.getId()).orElseThrow();
        contactUp.setName(contact.getName());
        contactUp.setAddress(contact.getAddress());
        contactUp.setEmail(contact.getEmail());
        contactUp.setFavourite(contact.isFavourite());
        contactUp.setLinkedinLink(contact.getLinkedinLink());
        contactUp.setPhoneNumber(contact.getPhoneNumber());
        
        contactRepo.save(contactUp);
        return contact;
    }

    @Override
    public List<contacts> getAllContacts() {
        return contactRepo.findAll();
    }

    @Override
    public contacts getById(String id) {
        return contactRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    @Override
    public void delete(String id) {

        contactRepo.deleteById(id);
    }

    @Override
    public List<contacts> search(String name, String email, String phoneNumber) {
        return new ArrayList<>();
    }

    @Override
    public List<contacts> getUserById(String id) {
        // contactRepo.findAllById(id);

        return new ArrayList<>();
    }

    @Override
    public Page<contacts> getByUser(Users user, int page, int size) {
        var pageable = PageRequest.of(page, size);

        return contactRepo.findByUser(user, pageable);

    }

    @Override
    public Page<contacts> searchByName(Users user, String name, int page, int size) {
        var pageable = PageRequest.of(page, size);

        return contactRepo.findByUserAndNameContaining(user, name, pageable);
    }

    @Override
    public Page<contacts> searchByEmail(Users user, String email, int page, int size) {
        var pageable = PageRequest.of(page, size);

        return contactRepo.findByUserAndEmailContaining(user, email, pageable);
    }

    @Override
    public Page<contacts> searchByPhone(Users user, String phoneNumber, int page, int size) {
        var pageable = PageRequest.of(page, size);

        return contactRepo.findByUserAndPhoneNumberContaining(user, phoneNumber, pageable);
    }

}
