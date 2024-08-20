package com.spring.security.config.jwt;

import com.spring.security.model.Token;
import com.spring.security.repository.TokenRepository;
import com.spring.security.service.ServiceJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final ServiceJWT serviceJWT;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getServletPath().contains("api/auth/")){
            filterChain.doFilter(request, response);
            return ;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String gmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        gmail = serviceJWT.extractUsername(jwt);
        if(gmail!=null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(gmail);
            Optional<Token> token=  tokenRepository.getTokenByToken(jwt);
            if(token.isEmpty()){
                filterChain.doFilter(request, response);
                return;
            }else if(token.get().isRevoked()&&token.get().isExpired()){
                filterChain.doFilter(request, response);
                return;
            }
            boolean isTokenValid = true;
            if(serviceJWT.isTokenValid(jwt, userDetails) && isTokenValid){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
