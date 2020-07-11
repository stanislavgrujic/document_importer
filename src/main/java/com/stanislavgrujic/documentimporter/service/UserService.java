package com.stanislavgrujic.documentimporter.service;

import com.stanislavgrujic.documentimporter.model.Role;
import com.stanislavgrujic.documentimporter.model.User;
import com.stanislavgrujic.documentimporter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

  private UserRepository userRepository;

  @Value("#{'${com.stanislavgrujic.documentimporter.instructors}'.split(',')}")
  private List<String> instructors;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public User create(UserDto userDto) {
    User user = new User();
    user.setEmail(userDto.getEmail());
    user.setFullName(userDto.getFullName());

    Role role = instructors.contains(userDto.getEmail()) ? Role.INSTRUCTOR : Role.LEARNER;
    user.setRole(role);

    return userRepository.save(user);
  }
}
