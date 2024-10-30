//package appointmenthospital.infoservice.config;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//
//public class KafkaConfig {
//    @Value("${spring.kafka.topic.user-service}")
//    private String topicName;
//
//    @Bean
//    public NewTopic topic() {
//        return TopicBuilder.name(topicName)
//                .build();
//    }
//}