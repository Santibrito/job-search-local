package com.jobsearch.localjobsearch.controller;

import com.jobsearch.localjobsearch.entity.Users;
import com.jobsearch.localjobsearch.service.IEmail;
import com.jobsearch.localjobsearch.service.IToken;
import com.jobsearch.localjobsearch.service.IUsers;
import com.jobsearch.localjobsearch.utils.LoggerHelper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final LoggerHelper logger = new LoggerHelper();

    @Autowired
    private IToken tokenService;


    @Autowired
    private IUsers userService;

    @Autowired
    private IEmail emailService;


    @GetMapping("logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }
        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }



    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody Users loginUser) {
        String email = loginUser.getEmail();
        String password = loginUser.getPassword();

        Users authenticatedUser = userService.authenticate(email, password);
        if (authenticatedUser != null) {
            String token = tokenService.generateToken(email);
            String userType = authenticatedUser.getUserType().getValue();

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("userType", userType);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o email inválido");
        }
    }


    @PostMapping("reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email, HttpServletRequest request) throws MessagingException {
        Users validationEmail= userService.findByEmail(email);

        if(validationEmail == null){
            logger.log(LoggerHelper.LogLevel.WARNING, "No se encontró email registrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró email registrado.");
        }

        String creteToken = tokenService.generateToken(email);
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");

        emailService.sendResetPassword(email,creteToken,baseUrl);

        return ResponseEntity.ok().build();
    }

    @PostMapping("confirm-password")
    public ResponseEntity<String> confirmPassword(@RequestParam String token, @RequestParam String newPassword, @RequestParam String passwordConfirm) {
        String userEmail = tokenService.getEmailFromToken(token);
        if(userEmail == null){
            logger.log(LoggerHelper.LogLevel.WARNING, "Token inválido.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token inválido.");
        }

        if (!newPassword.equals(passwordConfirm)) {
            logger.log(LoggerHelper.LogLevel.WARNING, "Las contraseñas no coinciden.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Las contraseñas no coinciden.");
        }

        try {
            userService.updatePassword(userEmail, newPassword);
            logger.log(LoggerHelper.LogLevel.INFO, "Contraseña restablecida con éxito.");
            return ResponseEntity.ok("Se ha restablecido la contraseña con éxito.");
        } catch (Exception e) {
            logger.log(LoggerHelper.LogLevel.ERROR, "Error al restablecer la contraseña: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al restablecer la contraseña.");
        }
    }

}
