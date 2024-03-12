package com.jobsearch.localjobsearch.service.impl;

import com.jobsearch.localjobsearch.service.IUsers;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.Collections;


import java.io.IOException;
import java.util.Collection;

@Service
public class JwtFilterImpl extends OncePerRequestFilter {
    private final TokenImpl jwtTokenProvider;
    private final IUsers userService;


    @Autowired
    public JwtFilterImpl(TokenImpl jwtTokenProvider,IUsers userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userRepository;
    }


    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Obtiene el token del encabezado de autorización de la solicitud
        String token = getTokenFromRequest(request);

        // Verifica si el token no es nulo y es válido
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // Si el token es válido, extrae el email del usuario del token
            String email = jwtTokenProvider.getEmailFromToken(token);
            String userType = jwtTokenProvider.getUserTypeByEmail(email);

            if (userType != null) {
                GrantedAuthority authority = new SimpleGrantedAuthority(userType);

                // Crea una colección que contenga el objeto GrantedAuthority
                Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

                // Crea el objeto UsernamePasswordAuthenticationToken con la colección de GrantedAuthority
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);

                // Establece la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
