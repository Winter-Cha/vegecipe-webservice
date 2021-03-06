package com.vegecipe.config.auth;

import com.vegecipe.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security 설정들을 활성화시켜 줍니다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() //h2-console화면을 사용하기 위해 해당 옵션들을 disable합니다.
                .and()
                    .authorizeRequests()
                    .antMatchers("/","/dist/**","/built/**","/plugins/**","/js/app/**", "/favicon.ico"
                                ,"/h2-console/**", "/profile","/login").permitAll()
                    .antMatchers("/community", "/posts", "/post/view/**").permitAll()
                    .antMatchers("/book", "/book/view/**").permitAll()
                    .antMatchers("/vegecipe","/googled7531326f1bd5fac.html").permitAll()
                    .antMatchers("/post/write","/post/update", "/api/v1/post", "/api/v1/post/**").permitAll()
                    .antMatchers("/api/v1/book/**").permitAll()
                    .antMatchers("/book/write", "/api/v1/books", "/api/v1/books/**").hasRole(Role.STAFF.name())
                    .anyRequest().authenticated()
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .loginPage("/login")
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);
    }
}
