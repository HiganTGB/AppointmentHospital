package appointmenthospital.authservice;

import appointmenthospital.authservice.client.FileStorageClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.processing.Generated;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
    
}
