package com.clothes.noc.mapper;

import com.clothes.noc.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueMappingStrategy;
import com.clothes.noc.dto.request.OAuth2RegisterRequest;
import com.clothes.noc.dto.request.RegisterRequest;
import com.clothes.noc.dto.request.UserProfileRequest;
import com.clothes.noc.dto.response.UserProfileResponse;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {
    User toUser(RegisterRequest request);

    User toUser(OAuth2RegisterRequest request);

    UserProfileResponse toUserProfileResponse(User user);

    void updateUser(@MappingTarget User user, UserProfileRequest request);
}
