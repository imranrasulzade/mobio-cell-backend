package com.example.msgateway.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.crypto.SecretKey;
import javax.naming.AuthenticationException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Slf4j
@Configuration
@PropertySource("classpath:application.yaml")
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secret_key;
    @Value("${application.security.jwt.expiration}")
    private long accessTokenValidity;
    private static Key key;


    public Key initializeKey(){
        byte[] keyBytes;
        keyBytes = Decoders.BASE64.decode(secret_key);
        key = Keys.hmacShaKeyFor(keyBytes);
        return key;
    }

//    private Claims parseJwtClaims(String token) {
//        return Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody();
//    }

    private Claims parseJwtClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret_key.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

//    @Deprecated
//    public Jwt<?, ?> decode(String token) {
//        try {
//            SecretKey key = Keys.hmacShaKeyFor(secret_key.getBytes(StandardCharsets.UTF_8));
//            return Jwts.parser()
//                    .verifyWith(key)
//                    .build()
//                    .parse(token);
//        } catch (JwtException e) {
//            throw new JwtException("Invalid JWT token", e);
//        }
//    }




//
//    public Claims resolveClaims(HttpServletRequest req) {
//        try {
//            String token = resolveToken(req);
//            if (token != null) {
//                return parseJwtClaims(token);
//            }
//            return null;
//        } catch (ExpiredJwtException ex) {
//            log.error("Error due to: {}", ex.getMessage());
//            req.setAttribute("expired", ex.getMessage());
//            throw ex;
//        } catch (Exception ex) {
//            log.error("Error due to: {}", ex.getMessage());
//            req.setAttribute("invalid", ex.getMessage());
//            throw ex;
//        }
//    }
//
//    public String resolveToken(HttpServletRequest request) {
//
//        String TOKEN_HEADER = "Authorization";
//        String bearerToken = request.getHeader(TOKEN_HEADER);
//        String TOKEN_PREFIX = "Bearer ";
//        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
//            return bearerToken.substring(TOKEN_PREFIX.length());
//        }
//        return null;
//    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }



    public Integer getUserId(Claims claims){
        return (Integer) claims.get("user_id");
    }



//    public Collection<GrantedAuthority> extractAuthorities(Claims claims) {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//        if (claims.containsKey("authorities")) {
//            List<String> roles = (List<String>) claims.get("authorities");
//            for (String role : roles) {
//                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
//            }
//        }
//        return authorities;
//    }

}
