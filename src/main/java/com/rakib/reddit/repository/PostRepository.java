package com.rakib.reddit.repository;

import com.rakib.reddit.model.Post;
import com.rakib.reddit.model.Subreddit;
import com.rakib.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
