package com.onlydive.onlydive.mapper;

import com.onlydive.onlydive.dto.UserDto;
import com.onlydive.onlydive.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto mapToResponse(User user);
}
