package com.process.jus.security.filters;

import com.process.jus.security.utils.JwtTokenUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

  private static final String AUTH_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    if (request.getCookies() != null && request.getCookies().length > 0) {
      Cookie[] cookies = request.getCookies();
      for (Cookie cookie : cookies) {
        if (cookie.getName().equalsIgnoreCase(AUTH_HEADER)) {
          userNameFromToken(request, cookie.getValue());
        }
      }
    } else {
      String token = request.getHeader(AUTH_HEADER);
      if (token != null && token.startsWith(BEARER_PREFIX)) {
        token = token.substring(7);
      }
      userNameFromToken(request, token);
    }
    chain.doFilter(request, response);
  }

  private void userNameFromToken(HttpServletRequest request, String token) {
    String username = jwtTokenUtil.getUsernameFromToken(token);
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
      if (jwtTokenUtil.tokenValido(token)) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
  }
}
