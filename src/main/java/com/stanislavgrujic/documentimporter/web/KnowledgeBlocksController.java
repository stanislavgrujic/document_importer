package com.stanislavgrujic.documentimporter.web;

import com.stanislavgrujic.documentimporter.model.Paragraph;
import com.stanislavgrujic.documentimporter.service.ParagraphService;
import com.stanislavgrujic.documentimporter.web.dto.KnowledgeBlocksResponseDto;
import com.stanislavgrujic.documentimporter.web.dto.ParagraphDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/knowledgeblocks")
public class KnowledgeBlocksController {

  @Autowired
  private ParagraphService service;

  @GetMapping
  public ResponseEntity<KnowledgeBlocksResponseDto> getKnowledgeBlocks(@RequestParam(required = false) String title) {
    List<Paragraph> paragraphs = service.findKnowledgeBlocks(title);
    List<ParagraphDto> paragraphDtos = paragraphs.stream()
                                                 .map(this::collectWithChildren)
                                                 .flatMap(List::stream)
                                                 .collect(Collectors.toList());

    return ResponseEntity.ok(new KnowledgeBlocksResponseDto(paragraphDtos));
  }

  private List<ParagraphDto> collectWithChildren(Paragraph paragraph) {
    List<ParagraphDto> paragraphs = new ArrayList<>();
    paragraphs.add(ParagraphDto.from(paragraph));

    paragraph.getChildren().forEach(child -> paragraphs.addAll(collectWithChildren(child)));
    return paragraphs;
  }
}
