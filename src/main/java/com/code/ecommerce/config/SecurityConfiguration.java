package com.code.ecommerce.config;

import com.okta.spring.boot.oauth.Okta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
public class SecurityConfiguration {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Protect endpoint /api/orders
        http.authorizeHttpRequests(requests ->
                requests.requestMatchers("/api/orders/**")
                        .authenticated()
                        .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(Customizer.withDefaults()));
        // + CORS filters
        http.cors(Customizer.withDefaults());

        // + content negotiation strategy
        http.setSharedObject(ContentNegotiationStrategy.class, new HeaderContentNegotiationStrategy());

        // + not-empty response body
        Okta.configureResourceServer401ResponseBody(http);

        // we are not using cookies for session tracking >> disable CSRF protection
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
