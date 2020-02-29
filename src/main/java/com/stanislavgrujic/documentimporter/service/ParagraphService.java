package com.stanislavgrujic.documentimporter.service;

import com.stanislavgrujic.documentimporter.model.Paragraph;
import com.stanislavgrujic.documentimporter.repository.ParagraphRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ParagraphService {

  @Autowired
  private ParagraphRepository repository;

  public List<Paragraph> getTopics() {
    return repository.findByTitleIsNotNull();
  }

  public List<Paragraph> findKnowledgeBlocks(String title) {
    if (StringUtils.isEmpty(title)) {
      return repository.findAll();
    }
    return repository.findByTitleContaining(title);
  }

  public Paragraph findKnowledgeBlockById(long id) {
    return repository.getOne(id);
  }
}
