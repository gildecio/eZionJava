package com.seguranca.config;

import com.seguranca.security.JwtAuthenticationFilter;
import com.seguranca.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(401, "Unauthorized - " + authException.getMessage());
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendError(403, "Access Denied - " + accessDeniedException.getMessage());
                })
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Públicos - sem autenticação
                .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/refresh").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                
                // Segurança - apenas admin
                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasAnyAuthority("USUARIO_VIEW")
                .requestMatchers(HttpMethod.POST, "/api/usuarios/**").hasAnyAuthority("USUARIO_CREATE")
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasAnyAuthority("USUARIO_UPDATE")
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasAnyAuthority("USUARIO_DELETE")
                
                .requestMatchers(HttpMethod.GET, "/api/roles/**").hasAnyAuthority("ROLE_VIEW")
                .requestMatchers(HttpMethod.POST, "/api/roles/**").hasAnyAuthority("ROLE_CREATE")
                .requestMatchers(HttpMethod.PUT, "/api/roles/**").hasAnyAuthority("ROLE_UPDATE")
                .requestMatchers(HttpMethod.DELETE, "/api/roles/**").hasAnyAuthority("ROLE_DELETE")
                
                .requestMatchers(HttpMethod.GET, "/api/permissoes/**").hasAnyAuthority("PERMISSAO_VIEW")
                .requestMatchers(HttpMethod.POST, "/api/permissoes/**").hasAnyAuthority("PERMISSAO_CREATE")
                .requestMatchers(HttpMethod.PUT, "/api/permissoes/**").hasAnyAuthority("PERMISSAO_UPDATE")
                .requestMatchers(HttpMethod.DELETE, "/api/permissoes/**").hasAnyAuthority("PERMISSAO_DELETE")
                
                // Outro módulos - requer autenticação
                .requestMatchers("/api/empresas/**").authenticated()
                
                // Qualquer outra requisição requer autenticação
                .anyRequest().authenticated()
            )
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
