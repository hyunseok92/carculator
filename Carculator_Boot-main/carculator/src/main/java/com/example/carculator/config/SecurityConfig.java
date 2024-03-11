package com.example.carculator.config;

import com.example.carculator.jwt.JwtAuthFilter;
import com.example.carculator.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    private static final String[] PERMIT_URL_ARRAY = {
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",

            /* resrvation */
            "/reservation/**",
            "/reservations/**",
            "/mReservation/**",
            "/mdReservation/**",
            "/repairshop/rate/**",
            "/repsMap/**",

            /* shop */
            "/shopAdminList/**",
            "/car/**",
            "/repairshop/**",
            "/shop/**",
            "/success",
            "/fail/**",
            "/static/**",
            "/csrf-token-endpoint/**",
            "/product/**",

            /* member & login */
            "/member/**",
            "/email/**",
            "/login/**",
            "/loginForm/**",
            "/join",
            "/kakaoLogin/**",
            "/admin/**",
            "/forgotpassword/**",
            "/changePwd",
            "/naverLogin/**",

            /* damage */
            "/damage/**",
            "/damage/img/**"


    };


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(Arrays.asList("*"));
        corsConfiguration.addAllowedOrigin("http://localhost:8888");
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "DELETE", "PUT", "PATCH"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) /* session을 사용하지 않음 */
                );


        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((auth) -> auth
                        //vue 구성 요소

                        .requestMatchers("/assets/**", "/js/**", "/fonts/**", "/favicon.ico", "/loader.css", "/productImages/**", "/Carculator/**").permitAll()
                        //swagger
                        .requestMatchers(PERMIT_URL_ARRAY).permitAll()
                        //LoginPermit
                        .requestMatchers("/**", "/index.html").permitAll()
                        //.requestMatchers("/admin").hasRole("ROLE_ADMIN")
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
