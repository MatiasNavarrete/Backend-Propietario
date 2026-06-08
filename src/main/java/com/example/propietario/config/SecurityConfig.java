package com.example.propietario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactivar CSRF para pruebas
                .cors(Customizer.withDefaults()) // Activar CORS para el Frontend
                .authorizeHttpRequests(auth -> auth
                        // 1. Permisos para Swagger y Docs
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // 2. Permisos para Consola H2
                        .requestMatchers("/h2-console/**").permitAll()
                        // 3. Permisos para tu API
                        .requestMatchers("/api/v1/propietario/**").permitAll()
                        // 4. Cualquier otra ruta requiere loguearse
                        .anyRequest().authenticated()
                )
                // Esto permite que el navegador muestre la consola H2 en un frame
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // Activa el login básico (por si acaso)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("fade")
                .password("{noop}123") // Asegúrate de que use builder() y el prefijo {noop}
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}