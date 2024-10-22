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
//                .route("user-service", r -> r.path("api/v1/user/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://user-service"))
//                .route("info-service", r -> r.path("api/v1/info-service/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://job-service"))
//                .route("job-service", r -> r.path("api/v1/appointment-service/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://appointment-service"))
                .route("test-service", r -> r.path("/api/v1/test/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://info-service"))
//                .route("notification-service", r -> r.path("api/v1/notification/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://notification-service"))
//
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://auth-service"))

//                .route("file-storage", r -> r.path("api/v1/file-storage/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("lb://file-storage"))
                .build();
    }
}