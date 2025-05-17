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



                // appointment-service
                .route("appointment-service", r -> r.path("/api/v1/doctors/**")
                        .uri("lb://appointment-service"))
                .route("appointment-service", r -> r.path("/api/v1/examinations/**")
                        .uri("lb://appointment-service"))
                .route("appointment-service", r -> r.path("/api/v1/users/**")
                        .uri("lb://appointment-service"))
                .route("appointment-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://appointment-service"))
                .route("appointment-service", r -> r.path("/api/v1/patients/**")
                        .uri("lb://appointment-service"))
                .route("auth-service", r -> r.path("/api/v1/profiles/**")
                        .uri("lb://appointment-service"))
                .route("appointment-service", r -> r.path("/api/v1/roles/**")
                        .uri("lb://appointment-service"))

                .route("file-storage", r -> r.path("/api/v1/file-storage/**")
                       // .filters(f -> f.filter(filter))
                        .uri("lb://file-storage"))

                .build();
    }
}