package com.oredata.bank.utils;

import com.oredata.bank.user.service.JwtService;
import com.oredata.bank.user.service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * we use cookie for that not store the token in FE, is in the browser cookie
 *
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailService userDetailsService;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(UserDetailService userDetailsService, JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = null;
        if(request.getCookies() != null && request.getCookies().length>0){
            cookie = Arrays.stream(request.getCookies()).filter(s-> s.getName().equals("cookieValue")).findAny().orElse(null);

        }
        if(cookie == null) {
            filterChain.doFilter(request,response);
            return;
        }
        if(cookie.getValue() == null){
            filterChain.doFilter(request,response);
            return;
        }
        final String jwt = cookie.getValue();
        final String email = jwtService.extractEmail(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(email == null || !userDetails.isAccountNonExpired() || !jwtService.validateToken(jwt)){
            filterChain.doFilter(request,response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails,null, Arrays.asList(new SimpleGrantedAuthority("ADMIN"))
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request,response);
    }
}
