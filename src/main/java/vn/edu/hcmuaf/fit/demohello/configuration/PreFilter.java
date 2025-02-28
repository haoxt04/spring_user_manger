package vn.edu.hcmuaf.fit.demohello.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class PreFilter extends OncePerRequestFilter {
    // hứng các request vào ug dụng rồi mới chuyển sang các api
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("-------------------PreFilter--------------------");

        // redirect sang các api
        filterChain.doFilter(request, response);
    }
}
