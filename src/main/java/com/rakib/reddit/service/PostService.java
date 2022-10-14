package com.rakib.reddit.service;

import com.rakib.reddit.dto.PostRequest;
import com.rakib.reddit.dto.PostResponse;
import com.rakib.reddit.dto.SubredditDto;
import com.rakib.reddit.exceptions.ResourceNotFoundException;
import com.rakib.reddit.model.Post;
import com.rakib.reddit.model.Subreddit;
import com.rakib.reddit.model.User;
import com.rakib.reddit.repository.PostRepository;
import com.rakib.reddit.repository.SubredditRepository;
import com.rakib.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final SubredditRepository subredditRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public void save(PostRequest postRequest) {
        String subredditName = postRequest.getSubredditName();
        Subreddit subreddit = this.subredditRepository.findByName(subredditName).orElseThrow(() -> new ResourceNotFoundException("Subreddit", "name", subredditName));
        User user = subreddit.getUser();

        Post post = this.modelMapper.map(postRequest, Post.class);
        post.setSubreddit(subreddit);
        post.setUser(user);
        this.postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id.toString()));

        PostResponse postResponse = this.modelMapper.map(post, PostResponse.class);
        postResponse.setSubredditName(post.getSubreddit().getName());
        postResponse.setUserName(post.getUser().getUsername());
        return postResponse;
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        List<Post> postList = postRepository.findAll();
        List<PostResponse> postResponseList = postList.stream().map((post) -> {
            PostResponse postResponse = this.modelMapper.map(post, PostResponse.class);
            postResponse.setSubredditName(post.getSubreddit().getName());
            postResponse.setUserName(post.getUser().getUsername());
            return postResponse;
        }).collect(Collectors.toList());
        return postResponseList;
    }

    public List<PostResponse> getPostsBySubreddit(Long id) {
        Subreddit subreddit = this.subredditRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subreddit", "id", id.toString()));
        List<Post> postList = this.postRepository.findAllBySubreddit(subreddit);
        List<PostResponse> postResponseList = postList.stream().map((post) -> {
            PostResponse postResponse = this.modelMapper.map(post, PostResponse.class);
            postResponse.setSubredditName(post.getSubreddit().getName());
            postResponse.setUserName(post.getUser().getUsername());
            return postResponse;
        }).collect(Collectors.toList());
        return postResponseList;
    }

    public List<PostResponse> getPostsByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User", "Username", username));
        List<Post> postList = postRepository.findByUser(user);
        List<PostResponse> postResponseList = postList.stream().map((post) -> {
            PostResponse postResponse = this.modelMapper.map(post, PostResponse.class);
            postResponse.setSubredditName(post.getSubreddit().getName());
            postResponse.setUserName(post.getUser().getUsername());
            return postResponse;
        }).collect(Collectors.toList());
        return postResponseList;
    }
}
