package com.stanislavgrujic.documentimporter.web.dto;

import lombok.Value;

import java.util.List;

@Value
public class TopicsResponseDto {

  private List<TopicDto> topicList;
}
