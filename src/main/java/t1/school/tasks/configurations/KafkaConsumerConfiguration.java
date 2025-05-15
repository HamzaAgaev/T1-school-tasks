package t1.school.tasks.configurations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import t1.school.tasks.entities.TaskEntity;
import t1.school.tasks.utils.CustomJsonDeserializer;

import org.springframework.context.annotation.Bean;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {

    private final KafkaProperties kafkaProperties;

    private Map<String, Object> buildCommonProperties() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomJsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "t1.school.tasks.entities.TaskEntity");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaProperties.getSessionTimeout());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, kafkaProperties.getMaxPartitionFetchBytes());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaProperties.getMaxPollRecords());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaProperties.getMaxPollIntervalMs());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getEnableAutoCommit());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getAutoOffsetResetConfig());
        // для обработки ошибок десериализации используем свой десериализатор
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, CustomJsonDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, CustomJsonDeserializer.class.getName());

        return props;
    }

    @Bean
    public ConsumerFactory<String, TaskEntity> consumerListenerFactory() {


        DefaultKafkaConsumerFactory<String, TaskEntity> factory = new DefaultKafkaConsumerFactory<>(buildCommonProperties());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskEntity> kafkaListenerContainerFactory(
            @Qualifier("consumerListenerFactory") ConsumerFactory<String, TaskEntity> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, TaskEntity> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(Integer.parseInt(kafkaProperties.getConcurrency()));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(Integer.parseInt(kafkaProperties.getPollTimeout()));
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.setCommonErrorHandler(commonErrorHandler());
        return factory;
    }

    private CommonErrorHandler commonErrorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(
                new FixedBackOff(
                        Long.parseLong(kafkaProperties.getRetryInterval()), Long.parseLong(kafkaProperties.getRetryAttempts())
                )
        );
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners((record, ex, attempt) ->
                log.error("Ошибка десериализации: " + ex.getMessage() + ", текущее смещение = " + record.offset() + ", попытка = " + attempt)
        );

        return handler;
    }
}
