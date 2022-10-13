package com.rakib.reddit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private Integer status;
    private boolean isSuccess;
    private String message;
    private T data;
}
