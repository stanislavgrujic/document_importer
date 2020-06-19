package com.stanislavgrujic.documentimporter.web.security;

import com.stanislavgrujic.documentimporter.model.User;
import com.stanislavgrujic.documentimporter.service.UserDto;
import com.stanislavgrujic.documentimporter.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

  private UserService userService;

  private JwtTokenUtil jwtTokenUtil;

  public AuthenticationService(UserService userService, JwtTokenUtil jwtTokenUtil) {
    this.userService = userService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  public String login(UserDto userDto) {
    User user = userService.findByEmail(userDto.getEmail());

    if (user == null) {
      user = userService.create(userDto);
    }

    String jwtToken = jwtTokenUtil.createToken(user);
    return jwtToken;
  }
}
