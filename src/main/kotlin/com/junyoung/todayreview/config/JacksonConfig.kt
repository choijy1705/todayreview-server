package com.junyoung.todayreview.config

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.format.DateTimeFormatter

@Configuration
class JacksonConfig {

    @Bean
    fun jacksonCustomizer(): Jackson2ObjectMapperBuilderCustomizer =
        Jackson2ObjectMapperBuilderCustomizer { builder ->
            val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
            val yearMonthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

            builder.serializers(
                LocalDateSerializer(dateFormatter),
                YearMonthSerializer(yearMonthFormatter),
            )
            builder.deserializers(
                LocalDateDeserializer(dateFormatter),
                YearMonthDeserializer(yearMonthFormatter),
            )
        }
}
