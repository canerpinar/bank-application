package com.oredata.bank.user.service;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${jwt.timeout}")
    private Long jwtTimeout;

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;


    public String extractEmail(String jwtToken) {
        String subject = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(jwtToken).getBody().getSubject();
        return subject;
    }

    public boolean validateToken(String jwt) {
        try{
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(jwt);
            if(claimsJws.getBody().getExpiration().before(new Date())) return false;
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            throw e;
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            throw new ExpiredJwtException(null,null,null);
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }
        return false;
    }

    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                .setExpiration(Date.from(Instant.now().plus(3,ChronoUnit.HOURS)))
                .signWith(SignatureAlgorithm.HS256,jwtSecretKey)
                .compact();

    }
}
