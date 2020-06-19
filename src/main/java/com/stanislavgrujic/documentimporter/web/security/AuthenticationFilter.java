package com.stanislavgrujic.documentimporter.web.security;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";

  private JwtTokenUtil jwtTokenUtil;

  public AuthenticationFilter(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String jwtToken = extractJwtToken(request);
    jwtTokenUtil.validate(jwtToken);

    filterChain.doFilter(request, response);
  }

  private String extractJwtToken(HttpServletRequest request) {
    String header = request.getHeader(AUTHORIZATION_HEADER);
    return StringUtils.hasText(header) && header.startsWith(AUTHORIZATION_TOKEN_PREFIX) ?
        header.substring(7) :
        null;
  }
}
