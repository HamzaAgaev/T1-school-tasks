package t1.school.tasks.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Function;

@Slf4j
public class CustomJsonDeserializer<T> extends JsonDeserializer<T> {

    private String getMessage(byte[] data) {
        return new String(data, StandardCharsets.UTF_8);
    }

    private T safeDeserialize(Function<Void, T> deserializer, byte[] data) {
        try {
            return deserializer.apply(null);
        } catch (Exception exception) {
            log.error("Произошла ошибка при десериализации сообщения " + getMessage(data), exception);
            throw new SerializationException("Невозможно десериализовать объект " + Arrays.toString(data));
        }
    }

    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        return safeDeserialize(v -> super.deserialize(topic, headers, data), data);
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        return safeDeserialize(v -> super.deserialize(topic, data), data);
    }
}
