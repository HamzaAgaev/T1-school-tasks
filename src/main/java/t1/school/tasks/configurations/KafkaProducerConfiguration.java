package t1.school.tasks.configurations;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import t1.school.tasks.entities.TaskEntity;
import t1.school.tasks.kafka.KafkaTaskProducer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfiguration {

    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaTemplate<String, TaskEntity> taskTemplate(ProducerFactory<String, TaskEntity> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTaskProducer producerTask(
            @Qualifier("taskTemplate") KafkaTemplate<String, TaskEntity> template,
            @Value("${t1-school.kafka.topic.notifications}") String notificationTopic
    ) {
        template.setDefaultTopic(notificationTopic);
        return new KafkaTaskProducer(template);
    }

    @Bean
    public ProducerFactory<String, TaskEntity> producerTaskFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServer());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, kafkaProperties.getEnableIdempotence());

        return new DefaultKafkaProducerFactory<>(props);
    }
}
