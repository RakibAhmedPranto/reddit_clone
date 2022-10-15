package com.rakib.reddit.controller;

import com.rakib.reddit.dto.PostRequest;
import com.rakib.reddit.dto.PostResponse;
import com.rakib.reddit.dto.Response;
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
    public ResponseEntity<Response<String>> createPost(@RequestBody PostRequest postRequest) {
        this.postService.save(postRequest);
        Response<String> response = new Response<>(201, true, "Post Created Successfully", "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response<PostResponse>> getPost(@PathVariable Long id) {
        PostResponse post = postService.getPost(id);
        Response<PostResponse> response = new Response<>(200, true, "Post Found", post);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<Response<List<PostResponse>>> getAllPosts() {
        List<PostResponse> allPosts = postService.getAllPosts();
        Response<List<PostResponse>> response = new Response<>(200, true, "Post List Found", allPosts);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("by-subreddit/{id}")
    public ResponseEntity<Response<List<PostResponse>>> getPostsBySubreddit(@PathVariable Long id) {
        List<PostResponse> allPosts = postService.getPostsBySubreddit(id);
        Response<List<PostResponse>> response = new Response<>(200, true, "Post List by Subreddit Id Found", allPosts);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("by-user/{name}")
    public ResponseEntity<Response<List<PostResponse>>> getPostsByUsername(@PathVariable String name) {
        List<PostResponse> allPosts = postService.getPostsByUsername(name);
        Response<List<PostResponse>> response = new Response<>(200, true, "Post List by User Found", allPosts);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
