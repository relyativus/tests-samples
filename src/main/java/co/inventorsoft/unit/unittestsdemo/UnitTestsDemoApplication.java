package co.inventorsoft.unit.unittestsdemo;

import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Instant;

@SpringBootApplication
public class UnitTestsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnitTestsDemoApplication.class, args);
    }

    @Bean
    public Jackson2ObjectMapperBuilder mapperBuilder() {
        final Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder = new Jackson2ObjectMapperBuilder();
        jackson2ObjectMapperBuilder.serializerByType(Instant.class, InstantSerializer.INSTANCE);
        jackson2ObjectMapperBuilder.deserializerByType(Instant.class, InstantDeserializer.INSTANT);
        return jackson2ObjectMapperBuilder;
    }
}
