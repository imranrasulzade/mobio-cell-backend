package com.example.msgateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtValidationGlobalFilter implements GlobalFilter, Ordered {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // api/auth-service/auth/sign-in
    // whitelist (optional)
    @Value("${application.security.jwt.whitelist:/auth/**,/actuator/health,/actuator/info,/api/auth-service/auth,/api/auth-service/auth/**}")
    private String whitelistCsv;

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        if (isWhitelisted(path)) {
            return chain.filter(exchange);
        }

        String token = resolveBearer(exchange.getRequest().getHeaders());
        if (!StringUtils.hasText(token)) {
            return unauthorized(exchange, "missing_token");
        }

        try {
            Claims claims = parseJwtClaims(token);

            // exp check
            Date exp = claims.getExpiration();
            if (exp == null || exp.before(new Date())) {
                return unauthorized(exchange, "token_expired");
            }

            return chain.filter(exchange);

        } catch (ExpiredJwtException e) {
            return unauthorized(exchange, "token_expired");
        } catch (JwtException e) {
            return unauthorized(exchange, "invalid_token");
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage(), e);
            return unauthorized(exchange, "invalid_token");
        }
    }

    private Claims parseJwtClaims(String token) {
        // Sənin createToken-da BASE64 decode edirsən:
        // byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        // key = Keys.hmacShaKeyFor(keyBytes);
        // Ona uyğun verify də BASE64 decode ilə olmalıdır.
        byte[] keyBytes;
        try {
            keyBytes = Decoders.BASE64.decode(secretKey);
        } catch (Exception ignore) {
            // secret base64 deyilsə fallback (səndə parseJwtClaims-də bu var idi)
            keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        }

        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String resolveBearer(HttpHeaders headers) {
        String auth = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(auth) && auth.startsWith("Bearer ")) {
            return auth.substring(7).trim();
        }
        return null;
    }

    private boolean isWhitelisted(String path) {
        if (!StringUtils.hasText(whitelistCsv)) return false;
        String[] patterns = whitelistCsv.split(",");
        for (String p : patterns) {
            String pattern = p.trim();
            if (pattern.isEmpty()) continue;
            if (matcher.match(pattern, path)) return true;
        }
        return false;
    }

    private String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (StringUtils.hasText(v)) return v;
        }
        return null;
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String code) {
        var res = exchange.getResponse();
        res.setStatusCode(HttpStatus.UNAUTHORIZED);
        res.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] body = ("{\"error\":\"unauthorized\",\"code\":\"" + code + "\",\"ts\":\"" + Instant.now() + "\"}")
                .getBytes(StandardCharsets.UTF_8);
        return res.writeWith(Mono.just(res.bufferFactory().wrap(body)));
    }
}
