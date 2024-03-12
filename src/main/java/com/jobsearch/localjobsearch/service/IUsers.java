package com.jobsearch.localjobsearch.service;

import com.jobsearch.localjobsearch.entity.Users;
import com.jobsearch.localjobsearch.enums.UserType;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUsers {
    List<Users> listAlll();

    Users findById(Long id);

    List<Users> getUsersByType(UserType userType);

    Users save(Users users);

    Users findByEmail(String email);

    Users updateUser(Users user) throws ChangeSetPersister.NotFoundException, IllegalAccessException;

    void delete(Users userId);

    @Transactional
    void updatePassword(String email, String newPassword) throws MessagingException;

    @Transactional(readOnly = true)
    Users authenticate(String email, String password);

}
