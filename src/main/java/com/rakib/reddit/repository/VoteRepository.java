package com.rakib.reddit.repository;

import com.rakib.reddit.model.Post;
import com.rakib.reddit.model.User;
import com.rakib.reddit.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
