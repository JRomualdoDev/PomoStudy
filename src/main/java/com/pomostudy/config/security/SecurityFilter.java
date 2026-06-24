package com.pomostudy.config.security;

import com.pomostudy.entity.User;
import com.pomostudy.exception.ResourceExceptionFactory;
import com.pomostudy.repository.UserRepository;
import com.pomostudy.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    TokenService tokenService;
    UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        recoverToken(request)
                .map(tokenService::validateToken)
                .flatMap(userRepository::findUserByEmail)
                .map(AuthenticatedUser::new)
                .ifPresent(this::authenticateUser);

        filterChain.doFilter(request, response);
    }

   private void authenticateUser(AuthenticatedUser user) {
        var auth = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
   }

   private Optional<String> recoverToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .filter(h -> h.startsWith("Bearer "))
                .map(h -> h.substring(7));
   }
}
