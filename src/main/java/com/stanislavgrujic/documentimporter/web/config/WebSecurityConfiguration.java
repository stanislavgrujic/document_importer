package com.stanislavgrujic.documentimporter.web.config;

import com.stanislavgrujic.documentimporter.model.Role;
import com.stanislavgrujic.documentimporter.service.UserService;
import com.stanislavgrujic.documentimporter.web.security.AuthenticationFilter;
import com.stanislavgrujic.documentimporter.web.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final String[] GET_ENDPOINTS = new String[] {"/api/topics", "/api/knowledgeblocks", "/api/user"};

  public static final String[] POST_ENDPOINTS = new String[] {"/api/knowledgeblocks"};

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  private UserService userService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests(authorizeRequests());
    http.oauth2Login(Customizer.withDefaults());
    http.oauth2Client();
    http.addFilterBefore(authenticationFilter(), BasicAuthenticationFilter.class);
    http.csrf().disable();
  }

  private Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> authorizeRequests() {
    return requests -> requests
        .antMatchers("/api/authenticate").permitAll()
        .antMatchers(HttpMethod.GET, GET_ENDPOINTS).hasAnyAuthority(Role.LEARNER.name(), Role.INSTRUCTOR.name())
        .antMatchers(HttpMethod.POST, POST_ENDPOINTS).hasAuthority(Role.INSTRUCTOR.name())
        .anyRequest().authenticated();
  }

  @Bean
  public AuthenticationFilter authenticationFilter() throws Exception {
    return new AuthenticationFilter(jwtTokenUtil, userService, authenticationManager());
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(new CustomAuthenticationProvider());
  }

  private static class CustomAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) {
      return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
      return true;
    }
  }

}
