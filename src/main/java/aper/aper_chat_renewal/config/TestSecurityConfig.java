package aper.aper_chat_renewal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class TestSecurityConfig {

    // TODO: 프로덕션 환경에서는 제거 필요.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/test/**").permitAll()  // Allow test endpoints without auth
                .requestMatchers("/api/**").permitAll()   // Allow API endpoints for testing
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable());  // Disable CSRF for testing
        
        return http.build();
    }
}