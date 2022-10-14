package com.rakib.reddit.service;

import com.rakib.reddit.dto.VoteDto;
import com.rakib.reddit.exceptions.GeneralApiException;
import com.rakib.reddit.exceptions.ResourceNotFoundException;
import com.rakib.reddit.model.Post;
import com.rakib.reddit.model.User;
import com.rakib.reddit.model.Vote;
import com.rakib.reddit.repository.PostRepository;
import com.rakib.reddit.repository.UserRepository;
import com.rakib.reddit.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.rakib.reddit.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final UserRepository userRepository;
    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId()).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", voteDto.getPostId().toString()));
        String currentUserEmail = authService.getCurrentUserEmail();
        User user = this.userRepository.findByEmailAndEnabledTrue(currentUserEmail).orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, user);

        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new GeneralApiException("You have already "
                    + voteDto.getVoteType() + "'d for this post");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }

        Vote vote = new Vote();
        vote.setVoteType(voteDto.getVoteType());
        vote.setPost(post);
        vote.setUser(user);
        voteRepository.save(vote);
        postRepository.save(post);

    }
}
