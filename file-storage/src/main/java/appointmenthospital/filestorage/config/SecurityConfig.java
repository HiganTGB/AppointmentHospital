package appointmenthospital.filestorage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/file-storage/v2/api-docs",
            "/file-storage/v3/api-docs",
            "/file-storage/v3/api-docs/**",
            "/file-storage/swagger-resources",
            "/file-storage/swagger-resources/**",
            "/file-storage/configuration/ui",
            "/file-storage/configuration/security",
            "/file-storage/swagger-ui/**",
            "/file-storage/webjars/**",
            "/file-storage/swagger-ui.html"};
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
                                .anyRequest().permitAll())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
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