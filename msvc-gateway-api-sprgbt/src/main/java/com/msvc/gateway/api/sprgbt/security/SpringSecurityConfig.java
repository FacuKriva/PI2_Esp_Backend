package com.msvc.gateway.api.sprgbt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SpringSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter authenticationFilter;

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers("/security/oauth/**").permitAll()
                .pathMatchers(HttpMethod.GET,"/security/tokens/revoke/**").permitAll()
                .pathMatchers(HttpMethod.POST,"/users").permitAll()
                .pathMatchers(HttpMethod.PUT,"/users/**").permitAll()
                .pathMatchers("/users/**").hasRole("ADMIN")
                .anyExchange().authenticated()
                .and().addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf().disable()
                .build();
    }
}
