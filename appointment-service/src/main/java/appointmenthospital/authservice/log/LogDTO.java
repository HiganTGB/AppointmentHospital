package appointmenthospital.authservice.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Map;
@Data
public class LogDTO {

    private String level;
    private String context;
    private String requestId;
    private LocalDateTime timestamp=LocalDateTime.now();

    public LogDTO(String level, String context, String requestId, Map<String, Object> metadata) {
        this.level = level;
        this.context = context;
        this.requestId = requestId;
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "LogDTO{" +
                "level='" + level + '\'' +
                ", context='" + context + '\'' +
                ", requestId='" + requestId + '\'' +
                ", timestamp=" + timestamp +
                ", metadata=" + metadata +
                '}';
    }

    private Map<String, Object> metadata;
}