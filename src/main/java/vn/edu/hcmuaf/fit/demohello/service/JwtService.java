package vn.edu.hcmuaf.fit.demohello.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    public String generateToken(UserDetails user);


}
