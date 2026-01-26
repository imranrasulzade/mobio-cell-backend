package com.example.msnumber.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PhoneNumberRequest {
    private String number;
    private Long userId;
    private Integer isMain;
    private Integer status;
}
