package com.onlydive.onlydive.mapper;

import com.onlydive.onlydive.dto.SpotDto;
import com.onlydive.onlydive.model.Spot;
import com.onlydive.onlydive.model.SpotComment;
import com.onlydive.onlydive.model.User;
import io.micrometer.common.lang.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class SpotMapper {
    protected final DateMapper dateMapper = new DateMapper();

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name",source = "spot.name")
    @Mapping(target = "latitude",source = "spot.latitude")
    @Mapping(target = "longitude",source = "spot.longitude")
    @Mapping(target = "comments",ignore = true)
    @Mapping(target = "creator",source = "user")
    @Mapping(target = "creationDate", expression = "java(java.time.Instant.now())")
    public abstract Spot mapToSpot(SpotDto spot, User user);

    @Mapping(target = "commentCount",expression = "java(countComments(spot))")
    @Mapping(target = "creatorUsername",source = "creator.username")
    @Mapping(target = "creationDate", expression = "java(dateMapper.mapToString(spot.getCreationDate()))")
    public abstract SpotDto mapToResponse(Spot spot);


    protected Long countComments(@NonNull Spot spot){
        List<SpotComment> comments = spot.getComments();
        if (comments == null)
            return 0L;
        return (long) comments.size();
    }

}
