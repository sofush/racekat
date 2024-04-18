package com.example.racekat.security;

import com.example.racekat.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Konfigurerer Spring Security modulet.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    /**
     * Opretter en SecurityFilterChain Bean som Spring Security vil bruge til at filtrere HTTP-anmodninger.
     * @return en "filter chain" dvs. en kæde af filtre.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // Kræver at alle HTTP-anmodninger skal autoriseres (med undtagelse af login siden, se nedenunder).
            // Kun adminstratorer kan tilgå "/admin" ruten.
            .authorizeHttpRequests((config) -> config
                .requestMatchers("/register/user").permitAll()
                .anyRequest().hasRole("USER")
            )
            .formLogin((config) -> config
                // Tillad alle besøgende at tilgå login siden.
                .loginPage("/login").permitAll()
            )
            // Tillad alle at logge ud.
            .logout(LogoutConfigurer::permitAll)
            // Tillader "HTTP Basic Authentication" hvilket vil sige at Spring Security
            // vil tjekke om der er brugernavn og password i de HTTP-anmodninger den filtrerer.
            .httpBasic(Customizer.withDefaults())
            .build();
    }

    /**
     * Opret en UserDetailsService Bean som Spring Security vil bruge til at hente brugerinformation fra
     * vores database (brugernavn, kodeord og roller).
     * @return en instans af DatabaseUserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new DatabaseUserDetailsService(userService);
    }

    /**
     * Opret en PasswordEncoder Bean.
     * @return en instans af en NoOpPasswordEncoder til at tjekke om passwords matcher.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new NoOpPasswordEncoder();
    }
}
