package org.x.cicada.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.x.cicada.utils.TokenUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TokenAuthFilter extends BasicAuthenticationFilter {


    public TokenAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = request.getHeader("Authorization");
        if (null != authorizationHeader) {
            String token = authorizationHeader.replace("Bearer ", "");

            try {
                Claims body = TokenUtils.getClaimsFromToken(token);
                String username = body.getSubject();
                List<String> authorities = Arrays.asList(new String[]{"hello:view"});
                Set<SimpleGrantedAuthority> simpleGrantedAuthorities =
                        authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities); // 1
                SecurityContextHolder.getContext().setAuthentication(authentication); // 2
            } catch (JwtException e) {
                throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
            }
        }

        chain.doFilter(request, response);
    }
}
