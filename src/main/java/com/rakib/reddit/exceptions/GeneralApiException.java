package com.rakib.reddit.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralApiException extends RuntimeException{
    String message;
    public GeneralApiException(String message){
        super(String.format("Message: %s",message));
        this.message = message;
    }
}
