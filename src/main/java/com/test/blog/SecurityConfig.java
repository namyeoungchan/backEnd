package com.test.blog;


import com.test.blog.Filter.TokenAuthenticationFilter;
//import com.test.blog.SecurityHandler.CustomAuthenticationProvider;
import com.test.blog.SecurityHandler.CustomLogoutSuccessHandler;
import com.test.blog.SecurityHandler.LoginFailureHandler;
import com.test.blog.SecurityHandler.LoginSuccessHandler;
import com.test.blog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
//    @Autowired
//    private CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용할 도메인을 설정합니다.
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 허용할 HTTP 메서드를 설정합니다.
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization")); // 허용할 요청 헤더를 설정합니다.
        configuration.setAllowCredentials(true); // 자격증명 허용 여부를 설정합니다.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ...
        http.csrf().disable().cors().and()
                // CORS preflight 요청 처리
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/loginUser").permitAll()
                .and().authorizeRequests()
                .antMatchers("/loginUser","/login","/signIn").permitAll() // 스웨거 접근 , 로그인 , 회원가입 접근가능 설정
                .anyRequest().authenticated() // 로그인한 사용자만 접근 가능한 페이지 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 생성 정책 설정
                .and()
                // 필터 설정
                .addFilterBefore(new TokenAuthenticationFilter(userService), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new TokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/logout") // 로그아웃 시 호출되는 URL
                .logoutSuccessHandler(new CustomLogoutSuccessHandler() {
                }); // 로그아웃 성공 시 호출되는 핸들러
    }

//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(customAuthenticationProvider).userDetailsService(userService).passwordEncoder(NoOpPasswordEncoder.getInstance());
//
//    }

}
