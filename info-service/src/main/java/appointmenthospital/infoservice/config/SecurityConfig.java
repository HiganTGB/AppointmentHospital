package appointmenthospital.infoservice.config;

import appointmenthospital.infoservice.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/info-service/v2/api-docs",
            "/info-service/v3/api-docs",
            "/info-service/v3/api-docs/**",
            "/info-service/swagger-resources",
            "/info-service/swagger-resources/**",
            "/info-service/configuration/ui",
            "/info-service/configuration/security",
            "/info-service/swagger-ui/**",
            "/info-service/webjars/**",
            "/info-service/swagger-ui.html"};
    private static final String[] OPEN_FEIGN_URL ={
            "/api/v1/doctor/{id}/domain"

    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request->request
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers(OPEN_FEIGN_URL).permitAll()
                                .requestMatchers("/api/v1/rooms/public/**","/api/v1/rooms/domain/**").permitAll()
                                .requestMatchers("/api/v1/specialties/public/**","api/v1/specialties/domain/**").permitAll()
                                .requestMatchers("/api/v1/test/demo-controller").hasAuthority("PER_CREATE_FOO")
                                .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(@NonNull CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedMethods("*");
//            }
//        };
//    }
}