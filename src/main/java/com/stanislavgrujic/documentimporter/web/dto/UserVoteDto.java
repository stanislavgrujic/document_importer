package com.stanislavgrujic.documentimporter.web.dto;

import lombok.Value;

@Value
public class UserVoteDto {

  long knowledgeBlockId;

  long vote;
}
