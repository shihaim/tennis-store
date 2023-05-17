package com.tnc.study.tennisstore.framework.security.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

/**
 * JWT를 위한 인증 토큰
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    @Serial
    private static final long serialVersionUID = 1235570856370748991L;

    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    @Override
    public boolean isAuthenticated() {
        return JwtUtil.validateToken(super.getCredentials().toString());
    }
}
