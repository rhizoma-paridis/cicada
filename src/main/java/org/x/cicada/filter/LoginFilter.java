package org.x.cicada.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.x.cicada.response.SimpleResponse;
import org.x.cicada.utils.TokenUtils;
import org.x.cicada.request.UserRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    public LoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserRequest param = new ObjectMapper()
                    .readValue(request.getInputStream(), UserRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    param.getUsername(),
                    param.getPassword()
            );
            return this.getAuthenticationManager().authenticate(authentication);
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = TokenUtils.generateToken(authResult.getName());
        ObjectMapper mapper = new ObjectMapper();
        response.addHeader("Content-Type", "application/json;charset=UTF-8");
        SimpleResponse<String> res = new SimpleResponse<>();
        res.setData(token);
        res.setStatus(1);
        res.setMsg("success");
        response.getWriter().write(mapper.writeValueAsString(res));
    }
}
