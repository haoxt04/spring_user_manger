package vn.edu.hcmuaf.fit.demohello.controller;


import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmuaf.fit.demohello.dto.request.SignInRequest;
import vn.edu.hcmuaf.fit.demohello.dto.response.TokenResponse;
import vn.edu.hcmuaf.fit.demohello.service.AuthenticationService;
import vn.edu.hcmuaf.fit.demohello.service.UserService;

@RestController
@RequestMapping("/auth")
@Validated
@Slf4j
@Tag(name = "Authentication Controller")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody SignInRequest request) {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }
}
