package com.jobsearch.localjobsearch.service;

import com.jobsearch.localjobsearch.entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

public interface IToken {
    String generateToken(String email);

    String getEmailFromToken(String token);

    boolean validateToken(String token);

    String getUserTypeByEmail(String email);

}
