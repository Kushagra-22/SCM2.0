package com.scm.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.scm.entities.Users;
import com.scm.entities.contacts;

@Repository
public interface ContactRepo extends JpaRepository<contacts, String> {

    Page<contacts> findByUser(Users user, Pageable pageable);

    Page<contacts> findByNameContaining(String nameKeyword, Pageable pageable);

    Page<contacts> findByEmailContaining(String emailKeyword, Pageable pageable);

    Page<contacts> findByPhoneNumberContaining(String phoneKeyword, Pageable pageable);

    Page<contacts> findByUserAndNameContaining(Users user, String name, PageRequest pageable);

    Page<contacts> findByUserAndEmailContaining(Users user, String email, PageRequest pageable);

    Page<contacts> findByUserAndPhoneNumberContaining(Users user, String phoneNumber, PageRequest pageable);

    // // custom query method
    // @Query("SELECT c FROM contacts c WHERE c.user.id = :userId")
    // List<contacts> findByUserId(@Param("userId") String userId);

    // Page<contacts> findByUserAndNameContaining(Users user, String
    // namekeyword,Pageable pageable);

    // Page<contacts> findByUserAndEmailContaining(Users user, String
    // emailkeyword,Pageable pageable);

    // Page<contacts> findByUserAndPhoneNumberContaining(Users user, String
    // phonekeyword, Pageable pageable);
}
