package com.stanislavgrujic.documentimporter.repository;

import com.stanislavgrujic.documentimporter.model.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {

  List<Paragraph> findByTitleIsNotNull();

  List<Paragraph> findByTitleContaining(String title);
}
