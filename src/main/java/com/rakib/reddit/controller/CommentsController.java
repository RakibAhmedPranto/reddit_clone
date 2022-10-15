package com.rakib.reddit.controller;

import com.rakib.reddit.dto.CommentsDto;
import com.rakib.reddit.dto.PostResponse;
import com.rakib.reddit.dto.Response;
import com.rakib.reddit.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Response<String>> createComment(@RequestBody CommentsDto commentsDto) {
        commentService.createComment(commentsDto);
        Response<String> response = new Response<>(201, true, "Commented Successfully", "");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/by-postId/{postId}")
    public ResponseEntity<Response<List<CommentsDto>>> getAllCommentsForPost(@PathVariable Long postId) {
        List<CommentsDto> commentByPost = commentService.getCommentByPost(postId);
        Response<List<CommentsDto>> response = new Response<>(200, true, "Comments Found for Post",
            commentByPost);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/by-user/{userName}")
    public ResponseEntity<Response<List<CommentsDto>>> getAllCommentsByUser(@PathVariable String userName) {
        List<CommentsDto> commentsByUser = commentService.getCommentsByUser(userName);
        Response<List<CommentsDto>> response = new Response<>(200, true, "Comments Found for User",
            commentsByUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
