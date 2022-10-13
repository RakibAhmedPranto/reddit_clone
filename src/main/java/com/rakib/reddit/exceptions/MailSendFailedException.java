package com.rakib.reddit.exceptions;

import javax.mail.SendFailedException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailSendFailedException extends SendFailedException {
    String message;

    public MailSendFailedException(String message){
        this.message=message;
    }
}
