package com.example.msnumber.response;

import lombok.Data;

@Data
public class PhoneNumberResponse {
    private Long id;
    private String number;
    private Long userId;
    private Integer isMain;
}
