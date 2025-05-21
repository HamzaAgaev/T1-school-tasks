package t1.school.tasks.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import t1.school.tasks.dtos.TaskDTO;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTaskProducer {

    private final KafkaTemplate<String, TaskDTO> template;

    public void sendToDefault(String key, TaskDTO taskDTO) {
        sendTo(template.getDefaultTopic(), key, taskDTO);
    }

    public void sendTo(String topic, String key, TaskDTO taskDTO) {
        template.send(topic, key, taskDTO)
            .thenAccept(
                sendResult -> {
                    RecordMetadata metadata = sendResult.getRecordMetadata();
                    TaskDTO sendTask = sendResult.getProducerRecord().value();
                    log.info("В топик " + metadata.topic() + " в партицию " + metadata.partition() + " было отправлено задание: " + sendTask);
                }
            ).exceptionally(
                exception -> {
                    log.error("При отправке производителем задания в брокер возникла ошибка: " + exception.getMessage(), exception);
                    return null;
                }
            );
    }
}
