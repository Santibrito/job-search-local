package com.jobsearch.localjobsearch.service.impl;

import com.jobsearch.localjobsearch.enums.UserType;
import com.jobsearch.localjobsearch.service.IEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailImpl implements IEmail {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendWelcomeEmail(String to, String username, UserType profileType) throws MessagingException {
        Context context = new Context();

        context.setVariable("username", username);

        String typeEmail = profileType.equals(UserType.CANDIDATE) ? "Welcome_Candidate" : "Welcome_Employees";
        String htmlBody = templateEngine.process(typeEmail, context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);

        helper.setSubject("Bienvenido");
        helper.setText(htmlBody, true);

        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendResetPassword(String to, String token, String baseUrl) throws MessagingException {
        Context context = new Context();

        String urlToken = baseUrl + "/user/reset/" + token;
        context.setVariable("ulrToken", urlToken);

        String htmlBody = templateEngine.process("Reset_Password", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);

        helper.setSubject("Restablecer la contraseña!");
        helper.setText(htmlBody, true);

        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendResetPasswordConfirm(String to, String username) throws MessagingException {
        Context context = new Context();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDateTime = now.format(formatter);

        context.setVariable("username", username);
        context.setVariable("date",formattedDateTime);

        String htmlBody = templateEngine.process("Reset_Password_Confirm", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);

        helper.setSubject("Restablecer la contraseña!");
        helper.setText(htmlBody, true);

        javaMailSender.send(mimeMessage);
    }

    @Override
    public void sendMessageEmployees(String to, String message) throws MessagingException {
        Context context = new Context();
        context.setVariable("message", message);

        String htmlBody = templateEngine.process("Send_Message", context);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);

        helper.setSubject("Nuevo mensaje");
        helper.setText(htmlBody, true);

        javaMailSender.send(mimeMessage);
    }

}
