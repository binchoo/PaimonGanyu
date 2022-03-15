package org.binchoo.paimonganyu.hoyopass.utils.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeStringConverter implements DynamoDBTypeConverter<String, LocalDateTime> {

    private final DateTimeFormatter dateTimeFormatter
            = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");

    @Override
    public String convert(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }

    @Override
    public LocalDateTime unconvert(String dateTimeString) {
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }
}