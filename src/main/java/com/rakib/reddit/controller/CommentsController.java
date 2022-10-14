package com.rakib.reddit.controller;

import com.rakib.reddit.dto.CommentsDto;
import com.rakib.reddit.dto.PostResponse;
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
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        commentService.createComment(commentsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@RequestParam("postId") Long postId) {
        List<CommentsDto> commentByPost = commentService.getCommentByPost(postId);
        return new ResponseEntity<List<CommentsDto>>(commentByPost,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CommentsDto>> getAllCommentsByUser(@RequestParam("userName") String userName) {
        List<CommentsDto> commentsByUser = commentService.getCommentsByUser(userName);
        return new ResponseEntity<List<CommentsDto>>(commentsByUser,HttpStatus.OK);
    }
}
