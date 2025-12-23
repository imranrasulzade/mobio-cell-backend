package com.example.msauth.service.impl;


import com.example.msauth.entity.User;
import com.example.msauth.enums.ExceptionCode;
import com.example.msauth.exception.NotFoundException;
import com.example.msauth.repositories.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.crypto.SecretKey;
import javax.naming.AuthenticationException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@PropertySource("classpath:application.yaml")
public class JwtService {

    private final UserRepository userRepository;
    @Value("${application.security.jwt.secret-key}")
    private String secret_key;
    @Value("${application.security.jwt.expiration}")
    private long accessTokenValidity;
    private static Key key;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Key initializeKey(){
        byte[] keyBytes;
        keyBytes = Decoders.BASE64.decode(secret_key);
        key = Keys.hmacShaKeyFor(keyBytes);
        return key;
    }
    public String createToken(User user) {
        key = initializeKey();
        user = userRepository.findByPhone(user.getPhone().trim())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_NOT_FOUND));

//        Map<String, Object> claimsMap = new HashMap<>();

        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        JwtBuilder jwtBuilder = Jwts.builder()
                .subject(user.getPhone())          // setSubject()
                .issuedAt(new Date())              // setIssuedAt()
                .expiration(tokenValidity)         // setExpiration()
                .signWith(key)     // yeni API: SignatureAlgorithm deyil, Jwts.SIG.*
                ;

        log.info("Jwt token created for user: {}", user.getPhone());

        return jwtBuilder.compact();
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





    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            log.error("Error due to: {}", ex.getMessage());
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Error due to: {}", ex.getMessage());
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String TOKEN_HEADER = "Authorization";
        String bearerToken = request.getHeader(TOKEN_HEADER);
        String TOKEN_PREFIX = "Bearer ";
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

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