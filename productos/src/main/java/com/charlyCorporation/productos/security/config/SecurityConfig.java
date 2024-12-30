package com.charlyCorporation.productos.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                    .authorizeHttpRequests()
                    .requestMatchers("/producto/no-seguro").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin().permitAll()
                    .and()
                    .httpBasic()
                    .and()
                    .build();
        }

}
