package com.bank.pos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/v1/transactions/**").permitAll()
                        .requestMatchers("/terminals", "/terminals/").hasAnyRole("ADMIN", "ACCOUNTANT")
                        .requestMatchers("/terminals/**").hasRole("ADMIN")
                        .requestMatchers("/exchange-rates/**").hasRole("ADMIN")
                        .anyRequest().hasAnyRole("ADMIN", "ACCOUNTANT")
                )
                .formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("admin")
                .roles("ADMIN")
                .build();
        UserDetails accountant = User.withDefaultPasswordEncoder()
                .username("accountant")
                .password("accountant")
                .roles("ACCOUNTANT")
                .build();
        return new InMemoryUserDetailsManager(admin, accountant);
    }
}
