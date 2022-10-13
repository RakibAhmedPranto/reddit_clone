package com.rakib.reddit.service;

import com.rakib.reddit.dto.SubredditDto;
import com.rakib.reddit.exceptions.ResourceNotFoundException;
import com.rakib.reddit.model.Subreddit;
import com.rakib.reddit.repository.SubredditRepository;
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

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        List<Subreddit> subreddits = this.subredditRepository.findAll();
        List<SubredditDto> subredditDtoList = subreddits.stream().map(subreddit -> this.modelMapper.map(subreddit, SubredditDto.class)).collect(Collectors.toList());
        return subredditDtoList;
    }

    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = this.modelMapper.map(subredditDto, Subreddit.class);
        Subreddit savedSubreddit = subredditRepository.save(subreddit);
        return this.modelMapper.map(savedSubreddit, SubredditDto.class);
    }

    @Transactional(readOnly = true)
    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subreddit Not Found with ","id", id.toString()));
        return this.modelMapper.map(subreddit,SubredditDto.class);
    }
}
