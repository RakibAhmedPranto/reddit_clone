package com.rakib.reddit.service;

import com.rakib.reddit.dto.CommentsDto;
import com.rakib.reddit.dto.PostResponse;
import com.rakib.reddit.exceptions.ResourceNotFoundException;
import com.rakib.reddit.model.Comment;
import com.rakib.reddit.model.Post;
import com.rakib.reddit.model.User;
import com.rakib.reddit.repository.CommentRepository;
import com.rakib.reddit.repository.PostRepository;
import com.rakib.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;
    public void createComment(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId()).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", commentsDto.getPostId().toString()));
        String currentUserEmail = authService.getCurrentUserEmail();
        User user = this.userRepository.findByEmailAndEnabledTrue(currentUserEmail).orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));

        Comment comment = new Comment();
        comment.setText(commentsDto.getText());
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedDate(Instant.now());
        commentRepository.save(comment);
    }

    public List<CommentsDto> getCommentByPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", postId.toString()));
        List<Comment> commentList = commentRepository.findByPost(post);
        List<CommentsDto> commentsDtoList = commentList.stream().map((comment) -> {
            CommentsDto commentsDto = this.modelMapper.map(comment, CommentsDto.class);
            commentsDto.setPostId(comment.getPost().getPostId());
            commentsDto.setUserName(comment.getUser().getUsername());
            return commentsDto;
        }).collect(Collectors.toList());
        return commentsDtoList;
    }

    public List<CommentsDto> getCommentsByUser(String userName) {
        User user = userRepository.findByUsername(userName).orElseThrow(() -> new ResourceNotFoundException("User", "UserName", userName));
        List<Comment> commentList = commentRepository.findAllByUser(user);
        List<CommentsDto> commentsDtoList = commentList.stream().map((comment) -> {
            CommentsDto commentsDto = this.modelMapper.map(comment, CommentsDto.class);
            commentsDto.setPostId(comment.getPost().getPostId());
            commentsDto.setUserName(comment.getUser().getUsername());
            return commentsDto;
        }).collect(Collectors.toList());
        return commentsDtoList;
    }
}
