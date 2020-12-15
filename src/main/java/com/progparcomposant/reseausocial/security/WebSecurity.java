package com.progparcomposant.reseausocial.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private static final String SCOPE_READ_MESSAGE = "SCOPE_read:messages";


    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.issuer}")
    private String issuer;

    @Bean
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .mvcMatchers("/register").permitAll()
                .mvcMatchers("/swagger-ui.html**").permitAll()
                .mvcMatchers("/swagger-ui.html/**").permitAll()
                .mvcMatchers("/users/**").hasAuthority(SCOPE_READ_MESSAGE)
                .mvcMatchers("/posts/**").hasAuthority(SCOPE_READ_MESSAGE)
                .mvcMatchers("/invitations/**").hasAuthority(SCOPE_READ_MESSAGE)
                .mvcMatchers("/friends/**").hasAuthority(SCOPE_READ_MESSAGE)
                .and().cors()
                .and().oauth2ResourceServer().jwt()
        ;
    }
}
