package appointmenthospital.scheduleservice.config;


import appointmenthospital.scheduleservice.jwt.JwtAuthenticationFilter;
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
            "/schedule-service/v2/api-docs",
            "/schedule-service/v3/api-docs",
            "/schedule-service/v3/api-docs/**",
            "/schedule-service/swagger-resources",
            "/schedule-service/swagger-resources/**",
            "/schedule-service/configuration/ui",
            "/schedule-service/configuration/security",
            "/schedule-service/swagger-ui/**",
            "/schedule-service/webjars/**",
            "/schedule-service/swagger-ui.html"};
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request->request.requestMatchers("/api/v1/test/demo-controller").hasAuthority("PER_CREATE_FOO")
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers(
//                "/swagger-resources/**",
//                "/swagger-ui.html/**",
//                "/swagger-resources/**",
//                "/swagger-ui/**",
//                "/v3/api-docs/**");
//    }
//
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