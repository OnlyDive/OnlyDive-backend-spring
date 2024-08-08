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

import java.util.Set;

@Mapper(componentModel = "spring")
@RequiredArgsConstructor
public abstract class SpotMapper {
    protected final DataMapper dataMapper = new DataMapper();

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments",ignore = true)
    @Mapping(target = "creator",source = "user")
    @Mapping(target = "creationDate", expression = "java(java.time.Instant.now())")
    public abstract Spot mapToSpot(SpotRequest dto, User user);

    @Mapping(target = "commentCount",expression = "java(countComments(spot))")
    @Mapping(target = "creatorUsername",source = "creator.username")
    @Mapping(target = "creationDate", expression = "java(dataMapper.mapToString(spot.getCreationDate()))")
    public abstract SpotResponse mapToResponse(Spot spot);


    protected Long countComments(@NonNull Spot spot){
        Set<SpotComment> comments = spot.getComments();
        if (comments == null)
            return 0L;
        return (long) comments.size();
    }

}
