package com.tnc.study.tennisstore.framework.security;

import com.tnc.study.tennisstore.framework.security.handler.CustomAccessDeniedHandler;
import com.tnc.study.tennisstore.framework.security.handler.CustomAuthenticationEntryPoint;
import com.tnc.study.tennisstore.framework.security.handler.LoginSuccessHandler;
import com.tnc.study.tennisstore.framework.web.ApiObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private static final RequestMatcher[] WHITE_LIST = {
            // 정적 리소스들에 대해서 모두 허용
            PathRequest.toStaticResources().atCommonLocations(),
            // h2 콘솔
            PathRequest.toH2Console(),
            // swagger-ui 허용
            AntPathRequestMatcher.antMatcher("/swagger-ui/**")
    };

    private final UserDetailsService loginService;
    private final ObjectPostProcessor<Object> objectPostProcessor;
    private final ApiObjectMapper apiObjectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Spring Security가 기본으로 제공하는 로그인 페이지 disable
        http.formLogin().disable();
//        http.headers().frameOptions().disable();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilter(usernamePasswordAuthenticationFilter());

        http.authorizeHttpRequests()
            .requestMatchers(WHITE_LIST).permitAll() // White-List 모두 허용
            .requestMatchers(AntPathRequestMatcher.antMatcher("/api/admin/**")).hasRole("ADMIN")
//            .requestMatchers(AntPathRequestMatcher.antMatcher("/api/admin/**")).hasAuthority("ADMIN")
            .anyRequest().authenticated(); // 어느 요청이던 인증을 하겠다.

        // 인증 / 권한 체크 실패 시 핸들링
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint()) // 인증 실패
                .accessDeniedHandler(accessDeniedHandler()); // 권한이 없을 때

        return http.build();
    }

    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() throws Exception {
        UsernamePasswordAuthenticationFilter authenticationFilter
                = new UsernamePasswordAuthenticationFilter(authenticationManager());

        authenticationFilter.setFilterProcessesUrl("/login");
        authenticationFilter.setPostOnly(true);

        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());

        return authenticationFilter;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new LoginSuccessHandler(apiObjectMapper);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new AuthenticationManagerBuilder(objectPostProcessor)
                .userDetailsService(loginService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint(apiObjectMapper);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler(apiObjectMapper);
    }
}
