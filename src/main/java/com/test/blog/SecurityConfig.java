package com.test.blog;


import com.test.blog.SecurityHandler.CustomAuthenticationProvider;
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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ...
        http.csrf().disable().authorizeRequests()
//                .antMatchers("/login","/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/webjars/**").permitAll() // 스웨거 접근 , 로그인 , 회원가입 접근가능 설정
                .antMatchers("/login","/signIn","/loginUser").permitAll() // 스웨거 접근 , 로그인 , 회원가입 접근가능 설정
                .anyRequest().authenticated() // 로그인한 사용자만 접근 가능한 페이지 설정
                .and()
                .formLogin()
                .loginProcessingUrl("/login") // 로그인 폼에서 로그인 버튼을 누르면 호출되는 URL
                .usernameParameter("username") // 로그인 폼에서 입력받는 사용자 ID 파라미터 이름
                .passwordParameter("password") // 로그인 폼에서 입력받는 사용자 비밀번호 파라미터 이름
                .successHandler(new LoginSuccessHandler()) // 로그인 성공 시 호출되는 핸들러
                .failureHandler(new LoginFailureHandler()) // 로그인 실패 시 호출되는 핸들러
                .and()
                .logout()
                .logoutUrl("/logout") // 로그아웃 시 호출되는 URL
                .logoutSuccessHandler(new CustomLogoutSuccessHandler() {
                }); // 로그아웃 성공 시 호출되는 핸들러
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider).userDetailsService(userService).passwordEncoder(NoOpPasswordEncoder.getInstance());

    }

}
