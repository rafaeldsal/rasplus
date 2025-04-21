package com.client.ws.rasmooplus.configuration;

import com.client.ws.rasmooplus.filter.AuthenticationFilter;
import com.client.ws.rasmooplus.repository.jpa.UserDetailsRepository;
import com.client.ws.rasmooplus.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private UserDetailsRepository userDetailsRepository;


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests((auth -> auth
            .requestMatchers("/subscription-type").permitAll()
            .requestMatchers("/subscription-type/*").permitAll()
            .requestMatchers(HttpMethod.POST, "/payment/process").permitAll()
            .requestMatchers(HttpMethod.POST, "/user").permitAll()
            .requestMatchers("/auth/recovery-code/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/auth").permitAll()
            .anyRequest().authenticated()))
        .httpBasic(Customizer.withDefaults())
        .addFilterBefore(new AuthenticationFilter(tokenService, userDetailsRepository), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
