package com.hrmcredixcam.config;

import com.hrmcredixcam.authdtos.ERole;
import com.hrmcredixcam.security.JwtAuthenticationEntryPoint;
import com.hrmcredixcam.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private final JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter authenticationFilter){
        this.authenticationEntryPoint = new JwtAuthenticationEntryPoint();
        this.authenticationFilter = authenticationFilter;
    }




    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
        return authConfiguration.getAuthenticationManager();
    }



    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/js/**", "/images/**");
    }
///v3/api-docs/**
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/api/**")
                                .permitAll()
                                //authentication
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/auth/signIn").permitAll()
                                .requestMatchers("/api/auth/register").permitAll()
                                .requestMatchers("/api/auth/refreshToken").permitAll()
                                .requestMatchers("/api/auth/forgot-password/**").permitAll()
                                .requestMatchers("/api/auth/verify-otp/**").permitAll()
                                .requestMatchers("/api/auth/reset-password/**").permitAll()
                                .requestMatchers("/api/auth/add/admin/**").permitAll()

                                //role
                                .requestMatchers("/api/roles/**").permitAll()
                                .requestMatchers("/api/roles/addRole/**").permitAll()

                ).exceptionHandling( exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                ).sessionManagement( session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
