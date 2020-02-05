package com.stanislavgrujic.documentimporter.web.dto;

import lombok.Value;

@Value
public class TopicDto {

  private Long id;
  private Long parentId;
  private String title;
}
