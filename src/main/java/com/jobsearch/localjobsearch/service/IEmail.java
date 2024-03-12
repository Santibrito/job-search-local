package com.jobsearch.localjobsearch.service;

import com.jobsearch.localjobsearch.enums.UserType;
import jakarta.mail.MessagingException;

public interface IEmail {
    void sendWelcomeEmail(String to, String username, UserType profileType) throws MessagingException;

    void sendResetPassword(String to, String token, String baseUrl) throws MessagingException;

    void sendResetPasswordConfirm(String to, String username) throws MessagingException;

    void sendMessageEmployees(String to, String message) throws MessagingException;

    interface IToken {
    }
}
