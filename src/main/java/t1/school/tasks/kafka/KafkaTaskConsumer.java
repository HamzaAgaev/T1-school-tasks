package t1.school.tasks.kafka;

import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import t1.school.tasks.entities.TaskEntity;
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

    private String generateMessageFromTask(TaskEntity task) {
        return "Задание с id = " + task.getId() + " было изменено. Теперь оно выглядит так: " + task + ".";
    }

    @KafkaListener(
            id = "${t1-school.kafka.consumer.group-id}",
            topics = "${t1-school.kafka.topic.notifications}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listener(
            @Payload List<TaskEntity> taskEntities,
            Acknowledgment ack,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {
        log.info("Новые сообщения топика " + topic + " с ключом " + key + " начинают обрабатываться потребителем");
        try {
            for (TaskEntity task: taskEntities) {
                notificationService.sendNotification("Было изменено задание", generateMessageFromTask(task));
                log.info("Сообщение об изменении задания " + task + " было успешно отправлено потребителем на почту");
            }
        } catch (Exception exception) {
            log.error("При отправке потребителем сообщения об обновлении задачи на почту возникла ошибка: " + exception.getMessage(), exception);
        } finally {
            try {
                ack.acknowledge();
            } catch (Exception exception) {
                log.error("Не удалось зафиксировать смещение при acknowledge: " + exception.getMessage(), exception);
            }
        }
    }
}