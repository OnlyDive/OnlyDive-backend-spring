package com.onlydive.onlydive.mapper;

import com.onlydive.onlydive.dto.SpotRequest;
import com.onlydive.onlydive.dto.SpotResponse;
import com.onlydive.onlydive.model.Spot;
import com.onlydive.onlydive.model.SpotComment;
import com.onlydive.onlydive.model.User;
import io.micrometer.common.lang.NonNull;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class SpotMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments",ignore = true)
    @Mapping(target = "creator",source = "user")
    @Mapping(target = "creationDate", expression = "java(java.time.Instant.now())")
    public abstract Spot mapToSpot(SpotRequest dto, User user);

    @Mapping(target = "commentCount",expression = "java(countComments(spot))")
    @Mapping(target = "creatorUsername",source = "creator.username")
    @Mapping(target = "creationDate", expression = "java(getDate(spot))")
    public abstract SpotResponse mapToResponse(Spot spot);


    protected Long countComments(@NonNull Spot spot){
        Set<SpotComment> comments = spot.getComments();
        if (comments == null)
            return 0L;
        return (long) comments.size();
    }

    protected String getDate(@NonNull Spot spot){
        Instant date = spot.getCreationDate();
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME
                .withZone(ZoneId.systemDefault())
                .format(date);
    }
}
