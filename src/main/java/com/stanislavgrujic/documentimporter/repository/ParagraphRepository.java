package com.stanislavgrujic.documentimporter.repository;

import com.stanislavgrujic.documentimporter.model.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {
}
