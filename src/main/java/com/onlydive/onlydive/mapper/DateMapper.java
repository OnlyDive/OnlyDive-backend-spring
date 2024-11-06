package com.onlydive.onlydive.mapper;

import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Mapper
public class DateMapper {

    public String mapToString(Instant date){
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(date);
    }
}

