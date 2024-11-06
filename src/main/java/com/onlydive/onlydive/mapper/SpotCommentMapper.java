package com.onlydive.onlydive.mapper;

import com.onlydive.onlydive.dto.SpotCommentDto;
import com.onlydive.onlydive.model.Spot;
import com.onlydive.onlydive.model.SpotComment;
import com.onlydive.onlydive.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class SpotCommentMapper {
    protected final DateMapper dateMapper = new DateMapper();

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name",source = "request.name")
    @Mapping(target = "description",source = "request.description")
    @Mapping(target = "creationDate", expression = "java(java.time.Instant.now())")
    public abstract SpotComment mapToSpotComment(SpotCommentDto request, Spot spot, User user);

    @Mapping(target = "spotId", source = "spotComment.spot.id")
    @Mapping(target = "username", source = "spotComment.user.username")
    @Mapping(target = "creationDate", expression = "java(dateMapper.mapToString(spotComment.getCreationDate()))")
    public abstract SpotCommentDto mapToSpotCommentResponse(SpotComment spotComment);
}
