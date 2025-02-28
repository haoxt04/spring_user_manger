package vn.edu.hcmuaf.fit.demohello.dto.response;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class TokenResponse implements Serializable {

    private String accessToken;

    private String refreshToken;

    private Long userId;

    // maybe some more
}
