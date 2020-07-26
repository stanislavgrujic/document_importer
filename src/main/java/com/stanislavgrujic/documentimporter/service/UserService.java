package com.stanislavgrujic.documentimporter.service;

import com.stanislavgrujic.documentimporter.model.Role;
import com.stanislavgrujic.documentimporter.model.User;
import com.stanislavgrujic.documentimporter.model.Vote;
import com.stanislavgrujic.documentimporter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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

  public User create(String email, String fullName) {
    User user = new User();
    user.setEmail(email);
    user.setFullName(fullName);

    Role role = instructors.contains(email) ? Role.INSTRUCTOR : Role.LEARNER;
    user.setRole(role);

    return userRepository.save(user);
  }

  public Set<Vote> getVotes(User votesOwner) {
    User user = userRepository.findByEmail(votesOwner.getEmail());
    return user.getVotes();
  }
}
