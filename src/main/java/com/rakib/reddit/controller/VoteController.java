package com.rakib.reddit.controller;

import com.rakib.reddit.dto.Response;
import com.rakib.reddit.dto.VoteDto;
import com.rakib.reddit.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes/")
@AllArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Response<String>> vote(@RequestBody VoteDto voteDto) {
        voteService.vote(voteDto);
        Response<String> voted_successfully = new Response<>(201, true, "Voted Successfully", "");
        return new ResponseEntity<>(voted_successfully, HttpStatus.CREATED);
    }
}
