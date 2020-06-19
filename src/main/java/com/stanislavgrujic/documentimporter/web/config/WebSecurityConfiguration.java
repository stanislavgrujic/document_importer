package com.stanislavgrujic.documentimporter.web.config;

import com.stanislavgrujic.documentimporter.web.security.AuthenticationFilter;
import com.stanislavgrujic.documentimporter.web.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests((requests) -> requests.anyRequest().authenticated());
    http.oauth2Login(Customizer.withDefaults());
    http.oauth2Client();
    http.addFilterBefore(authenticationFilter(), BasicAuthenticationFilter.class);
  }

  @Bean
  public AuthenticationFilter authenticationFilter() {
    return new AuthenticationFilter(jwtTokenUtil);
  }

}
