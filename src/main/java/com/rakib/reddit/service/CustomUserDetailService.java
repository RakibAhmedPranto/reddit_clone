package com.rakib.reddit.service;
import com.rakib.reddit.exceptions.ResourceNotFoundException;
import com.rakib.reddit.model.User;
import com.rakib.reddit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.findByEmailAndEnabledTrue(username)
            .orElseThrow(() -> new ResourceNotFoundException("User ", " email : ", username));
        return user;
    }

}
