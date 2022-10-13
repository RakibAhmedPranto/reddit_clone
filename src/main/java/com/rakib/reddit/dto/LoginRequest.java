package com.rakib.reddit.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
    @Email(message = "Email is not valid")
    @NotEmpty
    private String email;
    @NotEmpty
    @Size(min = 6,message = "Password Must be at least 6 character long ")
    private String password;
}
