package com.stanislavgrujic.documentimporter.web;

import com.stanislavgrujic.documentimporter.model.Paragraph;
import com.stanislavgrujic.documentimporter.service.ParagraphService;
import com.stanislavgrujic.documentimporter.web.dto.TopicDto;
import com.stanislavgrujic.documentimporter.web.dto.TopicsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
public class TopicsController {

  @Autowired
  private ParagraphService service;

  @GetMapping
  public ResponseEntity<TopicsResponseDto> topics() {
    List<Paragraph> paragraphs = service.getTopics();
    List<TopicDto> topics = paragraphs.stream()
                                      .map(paragraph -> {
                                        Long parentId = paragraph.getParent() == null ? null : paragraph.getParent().getId();
                                        return new TopicDto(paragraph.getId(), parentId, paragraph.getTitle());
                                      })
                                      .collect(Collectors.toList());
    return ResponseEntity.ok(new TopicsResponseDto(topics));
  }
}
