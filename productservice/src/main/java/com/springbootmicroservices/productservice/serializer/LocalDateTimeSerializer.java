package com.springbootmicroservices.productservice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom serializer for {@link LocalDateTime} objects.
 * This serializer formats the {@link LocalDateTime} as an ISO-8601 string.
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Serializes the given {@link LocalDateTime} value to an ISO-8601 formatted string.
     *
     * @param value the {@link LocalDateTime} value to serialize
     * @param gen the {@link JsonGenerator} used to write the serialized value
     * @param serializers the {@link SerializerProvider} that can be used to get serializers for other types
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.format(FORMATTER));
    }

}
