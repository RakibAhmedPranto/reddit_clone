package com.rakib.reddit.controller;

import com.rakib.reddit.dto.Response;
import com.rakib.reddit.dto.SubredditDto;
import com.rakib.reddit.service.SubredditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subreddit")
@AllArgsConstructor
public class SubredditController {
    private final SubredditService subredditService;

    @GetMapping
    public ResponseEntity<Response<List<SubredditDto>>> getAllSubreddits() {
        List<SubredditDto> dtoList = this.subredditService.getAll();
        Response<List<SubredditDto>> response = new Response<>(200, true, "Subreddit List Found", dtoList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<SubredditDto>> getSubreddit(@PathVariable Long id) {

        SubredditDto subreddit = this.subredditService.getSubreddit(id);
        Response<SubredditDto> response = new Response<>(200, true, "Subreddit Found", subreddit);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response<SubredditDto>> create(@RequestBody @Valid SubredditDto subredditDto) {
        SubredditDto dto = this.subredditService.save(subredditDto);
        Response<SubredditDto> response = new Response<>(201, true, "Subreddit Created Successfully", dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
