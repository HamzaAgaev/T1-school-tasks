package t1.school.tasks.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KafkaProperties {
    @Value("${t1-school.kafka.consumer.group-id}")
    private String groupId;

    @Value("${t1-school.kafka.consumer.enable-auto-commit}")
    private String enableAutoCommit;

    @Value("${t1-school.kafka.consumer.auto-offset-reset-config}")
    private String autoOffsetResetConfig;

    @Value("${t1-school.kafka.consumer.retry.interval.ms}")
    private String retryInterval;

    @Value("${t1-school.kafka.consumer.retry.attempts}")
    private String retryAttempts;

    @Value("${t1-school.kafka.consumer.listener.concurrency}")
    private String concurrency;

    @Value("${t1-school.kafka.consumer.listener.poll.timeout.ms}")
    private String pollTimeout;

    @Value("${t1-school.kafka.producer.enable-idempotence}")
    private String enableIdempotence;

    @Value("${t1-school.kafka.bootstrap.server}")
    private String server;

    @Value("${t1-school.kafka.session.timeout.ms}")
    private String sessionTimeout;

    @Value("${t1-school.kafka.max.partition.fetch.bytes}")
    private String maxPartitionFetchBytes;

    @Value("${t1-school.kafka.max.poll.records}")
    private String maxPollRecords;

    @Value("${t1-school.kafka.max.poll.interval.ms}")
    private String maxPollIntervalMs;

    @Value("${t1-school.kafka.topic.notifications}")
    private String notificationTopic;
}
