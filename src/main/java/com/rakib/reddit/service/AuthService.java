package com.rakib.reddit.service;

import static com.rakib.reddit.util.Constants.ACTIVATION_EMAIL;
import static com.rakib.reddit.util.Constants.ROLE_USER;
import static java.time.Instant.now;

import com.rakib.reddit.dto.RegisterRequest;
import com.rakib.reddit.exceptions.SpringRedditException;
import com.rakib.reddit.exceptions.ResourceNotFoundException;
import com.rakib.reddit.model.NotificationEmail;
import com.rakib.reddit.model.Role;
import com.rakib.reddit.model.User;
import com.rakib.reddit.model.VerificationToken;
import com.rakib.reddit.repository.RoleRepository;
import com.rakib.reddit.repository.UserRepository;
import com.rakib.reddit.repository.VerificationTokenRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;

    private final MailContentBuilder mailContentBuilder;

    private final ModelMapper modelMapper;



    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = this.modelMapper.map(registerRequest,User.class);

        Role role = this.roleRepository.findById(ROLE_USER).get();

        user.setPassword(this.encodePassword(registerRequest.getPassword()));
        user.setCreated(now());
        user.setEnabled(false);
        user.getRoles().add(role);
        this.userRepository.save(user);

        String token = this.generateVerificationToken(user);

        String message = mailContentBuilder.build("Thank you for signing up to Spring Reddit, please click on the below url to activate your account : "
            + ACTIVATION_EMAIL + "/" + token);

        mailService.sendMail(new NotificationEmail("Please Activate your account", user.getEmail(), message));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
//        MemoryCredentialsCache verificationTokenRepository;
        this.verificationTokenRepository.save(verificationToken);
        return token;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        this.fetchUserAndEnable(verificationTokenOptional.get());
    }

    @Transactional
    public Boolean logoutUserByRefreshToken(String email,String refreshToken) {

        User user = userRepository.findByEmailAndRefreshTokenAndEnabledTrue(email,refreshToken)
            .orElseThrow(() -> new ResourceNotFoundException("User", "Refresh Token", refreshToken));

        String dbRefreshToken = user.getRefreshToken();
        if(dbRefreshToken.equals(refreshToken)){
            user.setRefreshToken("");
            userRepository.save(user);
            return true;
        }else {
            throw new ResourceNotFoundException("User", "Refresh Token", refreshToken);
        }
    }

    @Transactional
    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String email = verificationToken.getUser().getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new SpringRedditException("User Not Found with email - " + email));
        user.setEnabled(true);
        userRepository.save(user);
    }
    @Transactional
    public void updateRefreshToken(String email, String refreshToken) {
        User user = this.userRepository.findByEmailAndEnabledTrue(email).orElseThrow(()->new ResourceNotFoundException("User","email",email));

        user.setRefreshToken(refreshToken);
        this.userRepository.save(user);
    }

    public Boolean checkUserAndRefreshToken(String email, String refreshtoken) {
        User user = this.userRepository.findByEmailAndEnabledTrue(email).orElseThrow(()->new ResourceNotFoundException("User","email",email));
        String dbRefreshToken = user.getRefreshToken();
        if(dbRefreshToken.equals(refreshtoken)){
            return true;
        }else {
            System.out.println("Token Miss matched");
            return false;
        }
    }

    public String getCurrentUserEmail(){
        User userDetail = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetail.getEmail();
    }
}
