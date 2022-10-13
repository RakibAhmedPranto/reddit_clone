package com.rakib.reddit.controller;

import com.rakib.reddit.dto.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ping")
@AllArgsConstructor
public class PingController {
    @PostMapping
    public ResponseEntity<String> signup() {
        return new ResponseEntity<>("Ping To Post Request Working", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> verifyAccount() {
        return new ResponseEntity<>("Ping To Guest Request Working", HttpStatus.OK);
    }
}
