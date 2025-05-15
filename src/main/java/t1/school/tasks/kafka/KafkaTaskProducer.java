package t1.school.tasks.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import t1.school.tasks.entities.TaskEntity;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTaskProducer {

    private final KafkaTemplate<String, TaskEntity> template;

    public void sendToDefault(String key, TaskEntity task) {
        sendTo(template.getDefaultTopic(), key, task);
    }

    public void sendTo(String topic, String key, TaskEntity task) {
        template.send(topic, key, task)
            .thenAccept(
                sendResult -> {
                    RecordMetadata metadata = sendResult.getRecordMetadata();
                    TaskEntity sendTask = sendResult.getProducerRecord().value();
                    log.info("В топик " + metadata.topic() + " в партицию " + metadata.partition() + " была отправлена задача: " + sendTask);
                }
            ).exceptionally(
                exception -> {
                    log.error("При отправке производителем задачи в брокер возникла ошибка: " + exception.getMessage(), exception);
                    return null;
                }
            );
    }
}
