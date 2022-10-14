package com.rakib.reddit.controller;

import com.rakib.reddit.dto.PostRequest;
import com.rakib.reddit.dto.PostResponse;
import com.rakib.reddit.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {
    private final PostService postService;
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        this.postService.save(postRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        PostResponse post = postService.getPost(id);
        return new ResponseEntity<PostResponse>(post,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> allPosts = postService.getAllPosts();
        return new ResponseEntity<List<PostResponse>>(allPosts,HttpStatus.OK);
    }

    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(Long id) {
        List<PostResponse> allPosts = postService.getPostsBySubreddit(id);
        return new ResponseEntity<List<PostResponse>>(allPosts,HttpStatus.OK);
    }

    @GetMapping("by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(String username) {
        List<PostResponse> allPosts = postService.getPostsByUsername(username);
        return new ResponseEntity<List<PostResponse>>(allPosts,HttpStatus.OK);
    }
}
