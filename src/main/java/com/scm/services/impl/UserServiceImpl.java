package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.Users;
import com.scm.helper.AppConstraints;
import com.scm.repo.UserRepo;
import com.scm.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Users saveUser(Users user) {
        // user id : have to generate
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        // password encode
        // user.setPassword(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // set the user role

        // user.setRoleList(List.of(AppConstants.ROLE_USER));

        user.setRoleList(List.of(AppConstraints.ROLE_USER));
        logger.info(user.getProvider().toString());
        String emailToken = UUID.randomUUID().toString();
        // user.setEmailToken(emailToken);
        Users savedUser = userRepo.save(user);
        // String emailLink = helper.getLinkForEmailVerificatiton(emailToken);
        // emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart Contact
        // Manager", emailLink);
        return savedUser;
    }

    @Override
    public Optional<Users> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Optional<Users> updateUser(Users user) {

        Users user2 = userRepo.getById(user.getUserId());
        // update karenge user2 from user
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setProvider(user.getProvider());

        // save the user in database
        Users save = userRepo.save(user2);
        return Optional.ofNullable(save);

    }

    @Override
    public void deleteUser(String id) {
        Users userUpdate = userRepo.findById(id).orElseThrow();
        userRepo.delete(userUpdate);

    }

    @Override
    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepo.findByEmail(email);

    }

    @Override
    public boolean isUserExistByEmail(String email) {
        // TODO Auto-generated method stub
        return false;    }

}
