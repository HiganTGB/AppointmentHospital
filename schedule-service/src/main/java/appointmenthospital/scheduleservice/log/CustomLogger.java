package appointmenthospital.scheduleservice.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CustomLogger {
    private static final Logger logger = LoggerFactory.getLogger(CustomLogger.class);

    public void log(LogDTO logDTO) {
        logger.info("Timestamp: {}, Level: {}, Context: {}, Metadata: {}",
                logDTO.getTimestamp(), logDTO.getLevel(), logDTO.getContext(), logDTO.getMetadata());
    }
    public void log(LogDTO logDTO,String simpleMetadata)
    {
        logger.info("Timestamp: {}, Level: {}, Context: {}, Metadata: {}",
                logDTO.getTimestamp(), logDTO.getLevel(), logDTO.getContext(), simpleMetadata);
    }
    public void log(String context,String simpleMetadata){
        logger.info("Timestamp: {}, Level: {}, Context: {}, Metadata: {}",
                LocalDateTime.now(),"INFO",context,simpleMetadata);
    }


}