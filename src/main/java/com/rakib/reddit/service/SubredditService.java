package com.rakib.reddit.service;

import com.rakib.reddit.dto.SubredditDto;
import com.rakib.reddit.exceptions.ResourceNotFoundException;
import com.rakib.reddit.model.Subreddit;
import com.rakib.reddit.model.User;
import com.rakib.reddit.repository.SubredditRepository;
import com.rakib.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        List<Subreddit> subreddits = this.subredditRepository.findAll();
        List<SubredditDto> subredditDtoList = subreddits.stream().map((subreddit) -> {
            SubredditDto dto = this.modelMapper.map(subreddit, SubredditDto.class);
            dto.setPostCount(subreddit.getPosts().size());
            return dto;
        }).collect(Collectors.toList());
        return subredditDtoList;
    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = this.modelMapper.map(subredditDto, Subreddit.class);
        String currentUserEmail = authService.getCurrentUserEmail();
        User user = this.userRepository.findByEmailAndEnabledTrue(currentUserEmail).orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUserEmail));
        subreddit.setUser(user);
        Subreddit savedSubreddit = subredditRepository.save(subreddit);
        SubredditDto subredditDtoResp = this.modelMapper.map(savedSubreddit, SubredditDto.class);
        subredditDtoResp.setPostCount(savedSubreddit.getPosts().size());
        return subredditDtoResp;
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subreddit","id", id.toString()));
        SubredditDto dto = this.modelMapper.map(subreddit, SubredditDto.class);
        dto.setPostCount(subreddit.getPosts().size());
        return dto;
    }
}
