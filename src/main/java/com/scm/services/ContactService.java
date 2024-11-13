package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.scm.entities.Users;
import com.scm.entities.contacts;

@Service
public interface ContactService {

    // @Autowired
    // ContactRepo contactRepo;

    contacts save(contacts contact);

    contacts update(contacts contact);

    List<contacts> getAllContacts();

    contacts getById(String id);

    void delete(String id);

    List<contacts> search(String name, String email, String phoneNumber);

    List<contacts> getUserById(String id);

    Page<contacts> getByUser(Users user, int page, int size);
    // Page<contacts> getByUser(Users user, int page, int size, String sortField,
    // String sortDirection);

    Page<contacts> searchByName(Users user,String name,int page, int size);

    Page<contacts> searchByEmail(Users user,String email,int page, int size);

    Page<contacts> searchByPhone(Users user,String phoneNumber,int page, int size);

}
