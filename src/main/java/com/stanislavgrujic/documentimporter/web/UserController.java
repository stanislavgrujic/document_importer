package com.stanislavgrujic.documentimporter.web;

import com.stanislavgrujic.documentimporter.model.User;
import com.stanislavgrujic.documentimporter.model.Vote;
import com.stanislavgrujic.documentimporter.service.UserService;
import com.stanislavgrujic.documentimporter.web.dto.UserVoteDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/user")
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/votes")
  public ResponseEntity<List<UserVoteDto>> getVotes(@AuthenticationPrincipal User authUser) {
    Set<Vote> votes = userService.getVotes(authUser);
    List<UserVoteDto> result = votes.stream()
                                    .map(vote -> new UserVoteDto(vote.getParagraph().getId(), vote.getVote()))
                                    .collect(toList());
    return ResponseEntity.ok(result);
  }
}
