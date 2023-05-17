package com.tnc.study.tennisstore.framework.security.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        boolean isApiRequest = requestURI.startsWith("/api/");

        // API 요청이 아니라면 다음 필터를 수행
        if (!isApiRequest) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Authorization 헤더가 없으면 다음 필터 수행
        if (!StringUtils.hasText(authorization)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String bearer = "Bearer ";

        // Bearer로 시작하지 않으면 다음 필터 수행
        if (!authorization.startsWith(bearer)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 로직이 여기까지 왔으면 JWT 토근을 Header에 담아서 보낸다는 것이다.
        String token = authorization.substring(bearer.length());
        boolean isValidToken = JwtUtil.validateToken(token);

        // 유효하지 않은 토큰
        if (!isValidToken) {
            log.error("Invalid Token...");

            // response에 에러가 있으면 AuthenticationEntryPoint.commence 메서드가 호출 됩니다.
            response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }

        // 토큰에서 권한과 userId를 추출
        Claims claims = JwtUtil.getClaimsFromToken(token);
        String subject = claims.getSubject();

        List<String> authorities = claims.get("authorities", List.class);
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(authorities.toArray(String[]::new));

        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(subject, token, authorityList);

        SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);

        filterChain.doFilter(request, response);
    }
}
