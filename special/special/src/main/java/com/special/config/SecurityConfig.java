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

        //SECURITY FILTER CHAIN
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrf -> csrf.disable())

                                //frame mono apo idio origin gia na mporei na emfanisei to h2-console
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.sameOrigin()))

                                //define which endpoints are public and which require authentication
                                .authorizeHttpRequests(auth -> auth
                                                .dispatcherTypeMatchers(DispatcherType.ERROR, DispatcherType.FORWARD)
                                                .permitAll() //allow error and forward dispatches
                                                .requestMatchers("/", "/home", "/register", "/login", "/error")
                                                .permitAll()
                                                // Allow access to all static resoures such as css,js,images etc
                                                .requestMatchers("/css/**", "/js/**", "/images/**", "/assets/**",
                                                                "/favicon.ico") //favicon irrelevant for now, but should be added to the static resources in production
                                                .permitAll()
                                                .anyRequest().authenticated()) // changed from authenticated() to
                                                                               // permitAll() for testing purposes,
                                                                               // should be changed back to
                                                                               // authenticated() in production

                                //form login
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .defaultSuccessUrl("/dashboard", true)
                                                .failureUrl("/login?error=true")
                                                .permitAll())

                                //remember me buutttonnnn
                                .rememberMe(remember -> remember
                                                .rememberMeParameter("remember-me")
                                                .rememberMeCookieName("remember-me")
                                                .key("uniqueAndSecretKey123") // Should be a secure, externalized
                                                                              // property in production
                                                .tokenValiditySeconds(86400 * 7)) //7 days

                                //clear session and cookies upon logging out gia na eimaste safe den pairnoume cookies
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login?logout")
                                                .deleteCookies("JSESSIONID", "remember-me")
                                                .invalidateHttpSession(true)
                                                .permitAll());

                return http.build();
        }

        //password encorder using bcrypt
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // epose the AuthenticationManager bean to be used for manual authentication if needed (ex in a custom login controller)
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }
}