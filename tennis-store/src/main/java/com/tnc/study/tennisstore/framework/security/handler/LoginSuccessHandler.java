package com.tnc.study.tennisstore.framework.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tnc.study.tennisstore.framework.security.jwt.JwtUtil;
import com.tnc.study.tennisstore.framework.web.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Map<String, Object> claims = Map.of("authorities", authorities);

        String token = JwtUtil.generateToken(username, claims);

        log.info("login success username: {}", username);
        log.info("login success authorities: {}", authorities);
        log.info("\n\n JWT token: {}", token);

        // 200 성공
        response.setStatus(HttpStatus.OK.value());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        ApiResponse apiResponse = ApiResponse.OK;
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
