package appointmenthospital.gateway.config;
import appointmenthospital.gateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final JwtAuthenticationFilter filter;

    public GatewayConfig(JwtAuthenticationFilter filter) {
        this.filter = filter;
    }
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("test-service", r -> r.path("/api/v1/test/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://info-service"))



                // Auth-Service
                .route("auth-service", r -> r.path("/api/v1/doctor/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/examination/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/user/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/patient/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/profile/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/diagnostic/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/role/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/appointment/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/medicine/**")
                        .uri("lb://auth-service"))
                .route("file-storage", r -> r.path("/api/v1/file-storage/**")
                       // .filters(f -> f.filter(filter))
                        .uri("lb://file-storage"))

                .build();
    }
}