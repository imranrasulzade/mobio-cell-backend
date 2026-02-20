package com.example.msbilling.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class InternalKeyAuthFilter extends OncePerRequestFilter {
    private static final String INTERNAL_KEY_HEADER = "X-Internal-Key";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    private static final String USER_ID_HEADER = "X-User-Id";
    private final String internalKey;
    private final String[] whitelist;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public InternalKeyAuthFilter(String internalKey, String[] whitelist) {
        this.internalKey = internalKey;
        this.whitelist = whitelist;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String pattern : whitelist) {
            if (matcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String key = request.getHeader(INTERNAL_KEY_HEADER);
        if (!StringUtils.hasText(internalKey) || !internalKey.equals(key)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getOutputStream()
                    .write("{\"error\":\"unauthorized\",\"message\":\"missing or invalid internal key\"}"
                            .getBytes(StandardCharsets.UTF_8));
            return;
        }

        String roleHeader = request.getHeader(USER_ROLE_HEADER);
        String userId = request.getHeader(USER_ID_HEADER);
        String resolvedRole = (roleHeader == null || roleHeader.isBlank()) ? "INTERNAL" : roleHeader.trim().toUpperCase();
        var authentication = new UsernamePasswordAuthenticationToken(
                userId != null ? userId : "internal-client",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + resolvedRole))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
