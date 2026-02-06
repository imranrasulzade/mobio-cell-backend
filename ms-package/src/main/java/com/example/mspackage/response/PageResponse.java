package com.example.mspackage.response;

import lombok.Data;

@Data
public class PageResponse {
    private int page;
    private int size;
    private long totalElements;
    private Object data;
}
