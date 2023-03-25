package com.shortener.url.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    SecurityConfig() {

    }

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    // http.cors().and().authorizeRequests()
    // .anyRequest()
    // .authenticated()
    // .and()
    // .httpBasic();

    // http.csrf().disable();

    // }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().authorizeHttpRequests((authz) -> authz
                .anyRequest().authenticated())
                .httpBasic();
        http.csrf().disable();
        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authentication)
            throws Exception {
        authentication.inMemoryAuthentication()
                .withUser("admin")
                .password("{noop}password")
                .authorities("ROLE_USER", "ROLE_ADMIN")
                .and()
                .withUser("userone")
                .password(
                        "{noop}password")
                .authorities("ROLE_USER")
                .and()
                .withUser("usertwo")
                .password(
                        "{noop}password")
                .authorities("ROLE_USER")
                .and()
                .withUser("userthree")
                .password(
                        "{noop}password")
                .authorities("ROLE_USER");
    }

}