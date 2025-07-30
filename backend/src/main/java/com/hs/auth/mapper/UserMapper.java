package com.hs.auth.mapper;

import com.hs.auth.dto.request.SignUpRequest;
import com.hs.auth.dto.response.UserResponse;
import com.hs.auth.entity.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    // SignUpRequest -> User 엔티티 변환
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(SignUpRequest request);

    // User 엔티티 -> UserResponse 변환
    UserResponse toResponse(User user);
}