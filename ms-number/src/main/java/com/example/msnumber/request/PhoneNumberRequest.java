package com.example.msnumber.request;

import lombok.Data;

@Data
public class PhoneNumberRequest {
    private String number;
    private Long userId;
    private Integer isMain;
    private Integer status;
}
