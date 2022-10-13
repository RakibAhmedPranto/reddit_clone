package com.rakib.reddit.controller;

import com.rakib.reddit.dto.LoginRequest;
import com.rakib.reddit.dto.RegisterRequest;
import com.rakib.reddit.dto.Response;
import com.rakib.reddit.jwt.JwtTokenHelper;
import com.rakib.reddit.jwt.response.JwtAuthResponse;
import com.rakib.reddit.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    private final UserDetailsService userDetailsService;

    private final JwtTokenHelper jwtTokenHelper;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
        this.authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Successful",HttpStatus.OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successully", HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<Response> signUp(@Valid @RequestBody LoginRequest request){
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());

        String accessToken = this.jwtTokenHelper.generateToken(userDetails);
        String refreshToken = this.jwtTokenHelper.generateRefreshToken(userDetails);
        String email = userDetails.getUsername();

        this.authService.updateRefreshToken(email,refreshToken);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(accessToken, refreshToken);
        Response<JwtAuthResponse> response = new Response<>(200,true,"Successfully Logged In",jwtAuthResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAccess")
    public ResponseEntity<Response> getAccessToken(@RequestHeader(name="Authorization") String token){
        String username = null;

        String refreshToken = null;
        if (token != null && token.startsWith("Bearer")) {
            refreshToken = token.substring(7);

            try {
                username = this.jwtTokenHelper.getUsernameFromToken(refreshToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get Jwt token");
            } catch (ExpiredJwtException e) {
                System.out.println("Jwt token has expired");
            } catch (MalformedJwtException e) {
                System.out.println("invalid jwt");
            } catch (SignatureException e){
                System.out.println("invalid jwt");
            }

            if(this.authService.checkUserAndRefreshToken(username,refreshToken)){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                String accessToken = this.jwtTokenHelper.generateToken(userDetails);

                JwtAuthResponse jwtAuthResponse = new JwtAuthResponse(accessToken,refreshToken);
                Response<JwtAuthResponse> response = new Response<>(200,true,"New Access Token Generated",jwtAuthResponse);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }else {
                Response response = new Response<>();
                response.setStatus(400);
                response.setMessage("Bad Token");
                response.setData("");
                response.setSuccess(false);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

        }else {
            Response response = new Response<>();
            response.setStatus(400);
            response.setMessage("Bad Token Name");
            response.setData("");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
