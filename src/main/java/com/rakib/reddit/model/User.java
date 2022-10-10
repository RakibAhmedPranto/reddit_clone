package com.rakib.reddit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import java.time.Instant;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long userId;
//    @NotBlank(message = "Username is required")
    private String username;
//    @NotBlank(message = "Password is required")
    private String password;
//    @Email
//    @NotEmpty(message = "Email is required")
    private String email;
    private Instant created;
    private boolean enabled;
}
