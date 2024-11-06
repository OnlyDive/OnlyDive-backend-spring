package com.onlydive.onlydive.config;

import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final SecretKey secretKey;

    /**this filter takes the header with the token, checks its correctness and expiration time.
     * Then it takes the user data and authorities from it and sends it to the security context holder.
     *
     * @param request : HttpServletRequest
     * @param response : HttpServletResponse
     * @param filterChain : FilterChain
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.isNullOrEmpty(authorization) || !authorization.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.replace("Bearer " , "");

        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token);

        Date expire = claimsJws.getPayload().getExpiration();
        Date now = new Date();

        if (expire.before(now)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request, response);
            return;
        }

        String username = claimsJws.getPayload().getSubject();
        var auths = (List<Map<String, String>>) claimsJws.getPayload().get("authorities");

        Set<SimpleGrantedAuthority> authority = auths.stream()
                .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                .collect(Collectors.toSet());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                username, null, authority
        );

            SecurityContextHolder.getContext().setAuthentication(auth);

        response.setStatus(HttpServletResponse.SC_OK);
        filterChain.doFilter(request, response);
    }
}
