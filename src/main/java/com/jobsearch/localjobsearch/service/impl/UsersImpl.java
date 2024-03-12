package com.jobsearch.localjobsearch.service.impl;

import com.jobsearch.localjobsearch.dao.UsersDao;
import com.jobsearch.localjobsearch.entity.Users;
import com.jobsearch.localjobsearch.enums.UserType;
import com.jobsearch.localjobsearch.service.IEmail;
import com.jobsearch.localjobsearch.service.IToken;
import com.jobsearch.localjobsearch.service.IUsers;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class UsersImpl implements IUsers {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UsersDao userService;

    @Autowired
    private IEmail emailService;


    @Transactional(readOnly = true)
    @Override
    public List<Users> listAlll() {
        return userService.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Users findById(Long id) {
        return userService.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Users> getUsersByType(UserType userType) {
        return userService.findByUserType(userType);
    }

    @Override
    public Users save(Users users) {
        return userService.save(users);
    }

    @Override
    public Users findByEmail(String email) {
        return userService.findByEmail(email);
    }

    @Override
    public Users updateUser(Users updatedUser) throws ChangeSetPersister.NotFoundException, IllegalAccessException {
        Users existingUser = userService.findById(updatedUser.getUserID())
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        Field[] fields = Users.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(updatedUser);
            if (value != null) {
                field.set(existingUser, value);
            }
        }
        return userService.save(existingUser);
    }

    @Transactional
    @Override
    public void delete(Users user) {
        userService.delete(user);
    }

    @Override
    @Transactional
    public void updatePassword(String email, String newPassword) throws MessagingException {
        Users user = userService.findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            String username = user.getFirstName() + " " + user.getLastName();
            emailService.sendResetPasswordConfirm(email, username);
            userService.save(user);
        } else {
            throw new RuntimeException("El usuario con el correo electrónico " + email + " no existe.");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Users authenticate(String email, String password) {
        Users user = userService.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
