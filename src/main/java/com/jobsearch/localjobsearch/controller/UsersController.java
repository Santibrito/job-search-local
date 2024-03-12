package com.jobsearch.localjobsearch.controller;

import com.jobsearch.localjobsearch.entity.Users;
import com.jobsearch.localjobsearch.enums.UserType;
import com.jobsearch.localjobsearch.service.IEmail;
import com.jobsearch.localjobsearch.service.IUsers;
import com.jobsearch.localjobsearch.utils.LoggerHelper;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/user")
public class UsersController {

    private static final LoggerHelper logger = new LoggerHelper();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private IUsers userService;

    @Autowired
    private IEmail emailService;

    @GetMapping("all")
    public ResponseEntity<?> getAllUsers() {
        List<Users> userList = userService.listAlll();
        Collections.reverse(userList);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        Users user = userService.findById(id);
        if(user == null){
            String errorMessage = "Usuario no existente.";
            logger.log(LoggerHelper.LogLevel.WARNING, errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("type/{userType}")
    public ResponseEntity<?> getUsersByType(@PathVariable UserType userType) {
        List<Users> users = userService.getUsersByType(userType);
        if (users.isEmpty()) {
            String errorMessage = "No existen usuarios de tipo " + userType.toString();
            logger.log(LoggerHelper.LogLevel.WARNING, errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("create")
    public ResponseEntity<?> createUser(@RequestBody Users users) {
        try {
            Users foundUser = userService.findByEmail(users.getEmail());
            if (foundUser != null && users.getEmail().equals(foundUser.getEmail())){
                String errorMessage = "Ya existe un usuario con esté email registrado.";
                logger.log(LoggerHelper.LogLevel.WARNING, errorMessage);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
            }
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            Users userSave = userService.save(users);
            emailService.sendWelcomeEmail(userSave.getEmail(), userSave.getFirstName() + " " + userSave.getLastName(), userSave.getUserType());
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataAccessException exception) {
            String errorMessage = "Error al crear usuario: " + exception.getMessage();
            logger.log(LoggerHelper.LogLevel.WARNING, errorMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        } catch (MessagingException e) {
            String errorMessage = "Error al enviar mensaje de bienvenida :" + e;
            logger.log(LoggerHelper.LogLevel.WARNING, errorMessage);
            throw new RuntimeException(e);
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> updateUser(@RequestBody Users user) throws ChangeSetPersister.NotFoundException, IllegalAccessException {
        try {
            Users updateUser = userService.updateUser(user);
            return new ResponseEntity<>(updateUser, HttpStatus.OK);
        } catch (ChangeSetPersister.NotFoundException e) {
            String errorMessage = "Error al actualizar usuario";
            logger.log(LoggerHelper.LogLevel.WARNING, errorMessage + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        try {
            Users user = userService.findById(id);
            if (user != null) {
                userService.delete(user);
                return new ResponseEntity<>("Usuario eliminado", HttpStatus.NO_CONTENT);
            } else {
                String errorMessage = "El usuario con el ID " + id + " no se encontró.";
                logger.log(LoggerHelper.LogLevel.WARNING, errorMessage);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
            }
        } catch (DataAccessException exception){
            String errorMessage = "Error al eliminar usuario.";
            logger.log(LoggerHelper.LogLevel.WARNING, errorMessage + exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}
