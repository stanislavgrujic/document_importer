package com.stanislavgrujic.documentimporter.web.security;

import com.stanislavgrujic.documentimporter.model.User;
import com.stanislavgrujic.documentimporter.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";

  private JwtTokenUtil jwtTokenUtil;
  private UserService userService;
  private AuthenticationManager authenticationManager;

  public AuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserService userService,
      AuthenticationManager authenticationManager) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.userService = userService;
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String jwtToken = extractJwtToken(request);
    String email = jwtTokenUtil.validate(jwtToken);

    if (email != null) {
      Authentication authentication = authenticateUser(email, jwtToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }

  private String extractJwtToken(HttpServletRequest request) {
    String header = request.getHeader(AUTHORIZATION_HEADER);
    boolean headerIsJwtAuthorization = StringUtils.hasText(header) && header.startsWith(AUTHORIZATION_TOKEN_PREFIX);
    return headerIsJwtAuthorization ? header.substring(7) : null;
  }

  private Authentication authenticateUser(String email, String token) {
    User user = userService.findByEmail(email);
    if (user == null) {
      return null;
    }

    GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
    PreAuthenticatedAuthenticationToken requestAuthentication =
        new PreAuthenticatedAuthenticationToken(user, token, Collections.singletonList(authority));
    return authenticationManager.authenticate(requestAuthentication);
  }

}
