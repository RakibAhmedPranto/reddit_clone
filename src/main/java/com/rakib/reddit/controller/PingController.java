package com.rakib.reddit.controller;

import com.rakib.reddit.dto.RegisterRequest;
import com.rakib.reddit.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ping")
@AllArgsConstructor
public class PingController {
    private final AuthService authService;
    @PostMapping
    public ResponseEntity<String> signup() {
        return new ResponseEntity<>("Ping To Post Request Working", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> verifyAccount() {
        return new ResponseEntity<>("Ping Working", HttpStatus.OK);
    }
}
