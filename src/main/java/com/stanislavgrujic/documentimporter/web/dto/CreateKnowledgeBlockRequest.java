package com.stanislavgrujic.documentimporter.web.dto;

import lombok.Value;

@Value
public class CreateKnowledgeBlockRequest {

  Long parentId;

  String title;

  String text;

  String sourceAuthor;

  String category;

  String seniority;
}
