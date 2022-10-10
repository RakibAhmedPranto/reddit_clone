package com.rakib.reddit.repository;

import com.rakib.reddit.model.Comment;
import com.rakib.reddit.model.Post;
import com.rakib.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
