package com.onlydive.onlydive.mapper;

import com.onlydive.onlydive.dto.UserResponse;
import com.onlydive.onlydive.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse mapToResponse(User user);
}
