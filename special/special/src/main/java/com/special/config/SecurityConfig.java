package com.special.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 🔐 SECURITY FILTER CHAIN
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF Protection
                .csrf(csrf -> csrf.disable())

                // SECURITY HEADERS: Prevent Clickjacking by ensuring the page can only be
                // framed by the same origin.
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin()))

                // AUTHORIZATION: Define which endpoints are public and which require
                // authentication.
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD).permitAll() // Allow error and forward dispatches
                        .requestMatchers("/","/register", "/login","/error").permitAll()
                        // Allow access to all static resources (CSS, JS, Images, Assets, Favicon)
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/assets/**", "/favicon.ico").permitAll()
                        .anyRequest().authenticated()) //changed from authenticated() to permitAll() for testing purposes, should be changed back to authenticated() in production

                // FORM LOGIN
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll())

                // REMEMBER ME BUTTON
                .rememberMe(remember -> remember
                        .key("uniqueAndSecretKey123") // Should be a secure, externalized property in production
                        .tokenValiditySeconds(86400 * 7)) // Valid for 7 days

                // LOGOUT: Clear session and cookies upon logging out
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .deleteCookies("JSESSIONID", "remember-me")
                        .invalidateHttpSession(true)
                        .permitAll());

        return http.build();
    }

    // PASSWORD ENCODER: Use BCrypt hashing algorithm for secure password storage.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AUTHENTICATION MANAGER: Expose the AuthenticationManager bean to be used for
    // manual authentication if needed.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}