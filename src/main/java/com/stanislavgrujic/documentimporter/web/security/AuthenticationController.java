package com.stanislavgrujic.documentimporter.web.security;

import com.stanislavgrujic.documentimporter.web.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthenticationController {

  private AuthenticationService authenticationService;

  public AuthenticationController(AuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @GetMapping("/api/authenticate")
  @ResponseBody
  public ResponseEntity<String> authenticate(OAuth2AuthenticationToken oAuthToken) {

    OAuth2User principal = oAuthToken.getPrincipal();
    UserDto userDto = new UserDto(principal.getAttribute("email"), principal.getAttribute("name"));
    String jwtToken = authenticationService.login(userDto);

    return ResponseEntity.ok(jwtToken);
  }
}
