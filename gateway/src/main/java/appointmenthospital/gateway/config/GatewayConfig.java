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
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/users/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/customers/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/doctors/**")
                        .uri("lb://auth-service"))
                .route("auth-service", r -> r.path("/api/v1/hospitals/**")
                        .uri("lb://auth-service"))
                  //
                // Info Service

                .route("info-service", r -> r.path("/api/v1/specialties/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://info-service"))
                .route("info-service", r -> r.path("/api/v1/rooms/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://info-service"))
                //


                .route("file-storage", r -> r.path("/api/v1/file-storage/**")
                       // .filters(f -> f.filter(filter))
                        .uri("lb://file-storage"))

                // Schedule Service
                .route("schedule-service", r -> r.path("/api/v1/schedule/**")
                        .filters(f->f.filter(filter))
                        .uri("lb://schedule-service"))

                .build();
    }
}