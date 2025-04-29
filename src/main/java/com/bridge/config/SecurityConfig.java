package com.bridge.config;

import com.bridge.filter.JwtFilter;
import com.bridge.utils.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;


    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String needFilter = RedisUtils.getFromRedis("needFilter");
        boolean enableJwt = Boolean.parseBoolean(needFilter);

        HttpSecurity security = http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    if (enableJwt) {
                        auth.requestMatchers(HttpMethod.POST, "players").permitAll();
                        auth.anyRequest().authenticated();
                    } else {
                        auth.anyRequest().permitAll();
                    }
                });

        if (enableJwt) {
            security.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        }

        return security.build();
    }
}
