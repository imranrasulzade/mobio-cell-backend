package com.example.msnumber.mapper;

import com.example.msnumber.entity.PhoneNumber;
import com.example.msnumber.request.PhoneNumberRequest;
import com.example.msnumber.response.PhoneNumberResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PhoneNumberMapper {

    public abstract PhoneNumber toEntity(PhoneNumberRequest request);
    public abstract PhoneNumberResponse toResponse(PhoneNumber entity);


}
