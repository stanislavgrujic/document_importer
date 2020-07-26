package com.stanislavgrujic.documentimporter.web.dto;

import lombok.Value;

import java.util.List;

@Value
public class UserVotesDto {

  List<UserVoteDto> votes;
}
