package com.stanislavgrujic.documentimporter.web.dto;

import lombok.Value;

import java.util.List;

@Value
public class KnowledgeBlocksResponseDto {

  private List<ParagraphDto> knowledgeBlockList;
}
