package vn.edu.hcmuaf.fit.demohello.service;

import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.demohello.exception.ResourceNotfoundException;
import vn.edu.hcmuaf.fit.demohello.model.Token;
import vn.edu.hcmuaf.fit.demohello.repository.TokenRepository;

import java.util.Optional;

@Service
public record TokenService(TokenRepository tokenRepository) {

    public Token getByUsername(String username) {
        return tokenRepository.findByUsername(username).orElseThrow(() -> new ResourceNotfoundException("Not found token"));
    }

    public int save(Token token) {
        Optional<Token> optional = tokenRepository.findByUsername(token.getUsername());
        if (optional.isEmpty()) {
            tokenRepository.save(token);
            return token.getId();
        } else {
            Token t = optional.get();
            t.setAccessToken(token.getAccessToken());
            t.setRefreshToken(token.getRefreshToken());
            tokenRepository.save(t);
            return t.getId();
        }
    }

    public void delete(String username) {
        Token token = getByUsername(username);
        tokenRepository.delete(token);
    }
}
