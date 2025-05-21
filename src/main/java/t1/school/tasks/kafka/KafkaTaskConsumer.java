package t1.school.tasks.kafka;

import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import t1.school.tasks.dtos.TaskDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import t1.school.tasks.services.NotificationService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTaskConsumer {

    private final NotificationService notificationService;

    private String generateMessageFromTask(TaskDTO taskDTO) {
        return "У задания с id = " + taskDTO.getId() + " был изменен статус. Теперь оно выглядит так: " + taskDTO + ".";
    }

    @KafkaListener(
            id = "${t1-school.kafka.consumer.group-id}",
            topics = "${t1-school.kafka.topic.notifications}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listener(
            @Payload List<TaskDTO> taskDTOs,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {
        log.info("Новые сообщения топика " + topic + " с ключом " + key + " начинают обрабатываться потребителем");
        try {
            for (TaskDTO taskDTO: taskDTOs) {
                notificationService.sendNotification("Был изменен статус задания", generateMessageFromTask(taskDTO));
                log.info("Сообщение об изменении статуса задания " + taskDTO + " было успешно отправлено потребителем на почту");
            }
        } catch (Exception exception) {
            log.error("При отправке потребителем сообщения об изменении статуса задания на почту возникла ошибка: " + exception.getMessage(), exception);
        } finally {
            try {
                ack.acknowledge();
            } catch (Exception exception) {
                log.error("Не удалось зафиксировать смещение при acknowledge: " + exception.getMessage(), exception);
            }
        }
    }
}