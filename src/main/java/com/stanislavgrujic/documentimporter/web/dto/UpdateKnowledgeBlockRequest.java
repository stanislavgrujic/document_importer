package com.stanislavgrujic.documentimporter.web.dto;

import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
public class UpdateKnowledgeBlockRequest {

  @NotNull
  Long id;

  Long parentId;

  String title;

  String text;

  String sourceAuthor;

  String category;

  String seniority;
}
