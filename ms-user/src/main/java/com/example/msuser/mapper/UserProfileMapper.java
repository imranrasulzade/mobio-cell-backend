package com.example.msuser.mapper;

import com.example.msuser.dto.UserProfileDto;
import com.example.msuser.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserProfileMapper {

    public abstract UserProfile toEntity(UserProfileDto dto);
    public abstract UserProfileDto toDto(UserProfile entity);

}
