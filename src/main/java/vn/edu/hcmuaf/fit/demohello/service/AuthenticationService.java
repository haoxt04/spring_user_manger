package vn.edu.hcmuaf.fit.demohello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.demohello.dto.request.SignInRequest;
import vn.edu.hcmuaf.fit.demohello.dto.response.TokenResponse;
import vn.edu.hcmuaf.fit.demohello.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public TokenResponse authenticate(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        var user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("username or password incorrect"));

        String accessToken = jwtService.generateToken(user);

        return TokenResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refresh_token")
                .userId(user.getId())
                .build();
    }
}
