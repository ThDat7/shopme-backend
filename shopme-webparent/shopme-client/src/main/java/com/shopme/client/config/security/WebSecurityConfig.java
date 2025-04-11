package com.shopme.client.config.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.shopme.client.service.CustomerContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import javax.crypto.spec.SecretKeySpec;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Value("${google.client.id}")
    private String googleClientId;

    private final CustomerContextService customerContextService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer ->
                        jwtConfigurer.decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                ).authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

        http.authorizeHttpRequests(request ->
                request
                        // Public endpoints - no authentication required
                        .requestMatchers(publicEndpoints()).permitAll()

                        // Protected endpoints - authentication required
                        .requestMatchers(authenticatedEndpoints()).access(authorizationManager())

                        // Fallback - require authentication for any other requests
                        .anyRequest().authenticated()
        );
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    private String[] publicEndpoints() {
        return new String[]{
                "/api/v1/auth/**",
                "/api/v1/customers/register",
                "/api/v1/customers/verify",
                "/api/v1/customers/resend-verification",
                "/api/v1/products/**",
                "/api/v1/categories/**",
                "/api/v1/brands/**",
                "/api/v1/promotions/**",
                "/api/v1/feature-categories/**",
                "/api/v1/locations/**",
                "/api/v1/reviews//product/**"
        };
    }

    private String[] authenticatedEndpoints() {
        return new String[]{
                "/api/v1/customers/profile",
                "/api/v1/customers/profile/**",
                "/api/v1/addresses/**",
                "/api/v1/cart/**",
                "/api/v1/orders/**",
                "/api/v1/checkout/**",
                "/api/v1/reviews/order-detail/**"
        };
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> authorizationManager() {
        return new CustomerStatusAuthorizationManager(customerContextService);
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), MacAlgorithm.HS512.name());
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    @Bean
    JsonFactory JsonFactory() {
        return GsonFactory.getDefaultInstance();
    }

    @Bean
    GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), JsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

}