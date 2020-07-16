package com.stanislavgrujic.documentimporter.web;

import com.stanislavgrujic.documentimporter.model.Attributes;
import com.stanislavgrujic.documentimporter.model.Level;
import com.stanislavgrujic.documentimporter.model.Paragraph;
import com.stanislavgrujic.documentimporter.model.Semantics;
import com.stanislavgrujic.documentimporter.service.ParagraphService;
import com.stanislavgrujic.documentimporter.web.dto.CreateKnowledgeBlockRequest;
import com.stanislavgrujic.documentimporter.web.dto.KnowledgeBlocksResponseDto;
import com.stanislavgrujic.documentimporter.web.dto.ParagraphDto;
import com.stanislavgrujic.documentimporter.web.dto.UpdateKnowledgeBlockRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/knowledgeblocks")
public class KnowledgeBlocksController {

  @Autowired
  private ParagraphService service;

  @GetMapping("/{id}")
  public ResponseEntity<ParagraphDto> getKnowledgeBlocksById(@PathVariable long id) {
    Paragraph paragraph = service.findKnowledgeBlockById(id);

    return ResponseEntity.ok().body(ParagraphDto.from(paragraph));
  }

  @GetMapping
  public ResponseEntity<KnowledgeBlocksResponseDto> getKnowledgeBlocks(@RequestParam String title,
      @RequestParam(required = false) String level) {
    List<Level> levels = Level.getLevels(level);

    List<Paragraph> paragraphs = service.findKnowledgeBlocks(title);
    List<ParagraphDto> paragraphDtos = paragraphs.stream()
                                                 .map(paragraph -> collectWithChildren(paragraph, levels))
                                                 .flatMap(List::stream)
                                                 .collect(Collectors.toList());

    return ResponseEntity.ok(new KnowledgeBlocksResponseDto(paragraphDtos));
  }

  @PostMapping
  public ResponseEntity<URI> createKnowledgeBlock(@RequestBody CreateKnowledgeBlockRequest request,
      UriComponentsBuilder uriBuilder) {
    Attributes attributes = new Attributes();
    attributes.setSourceAuthor(request.getSourceAuthor());
    attributes.setLevel(Level.valueOf(request.getSeniority()));
    attributes.setSemantics(Semantics.from(request.getCategory()));

    Paragraph paragraph = new Paragraph();
    paragraph.setTitle(request.getTitle());
    paragraph.setValue(request.getText());
    paragraph.setAttributes(attributes);

    long paragraphId = service.create(paragraph, request.getParentId());

    URI uri = uriBuilder.path("/api/knowledgeblocks/{id}").buildAndExpand(paragraphId).toUri();
    return ResponseEntity.created(uri).build();
  }

  @PutMapping
  public ResponseEntity<Void> updateKnowledgeBlock(@RequestBody @Valid UpdateKnowledgeBlockRequest request) {
    Attributes attributes = new Attributes();
    attributes.setSourceAuthor(request.getSourceAuthor());
    attributes.setLevel(Level.valueOf(request.getSeniority()));
    attributes.setSemantics(Semantics.from(request.getCategory()));

    Paragraph parent = new Paragraph();
    parent.setId(request.getParentId());

    Paragraph paragraph = new Paragraph();
    paragraph.setId(request.getId());
    paragraph.setTitle(request.getTitle());
    paragraph.setValue(request.getText());
    paragraph.setParent(parent);
    paragraph.setAttributes(attributes);

    service.update(paragraph);
    return ResponseEntity.ok().build();
  }

  private List<ParagraphDto> collectWithChildren(Paragraph paragraph, List<Level> matchingSeniorities) {

    List<ParagraphDto> paragraphs = new ArrayList<>();
    Attributes attributes = paragraph.getAttributes();
    if (attributes != null && matchingSeniorities.contains(attributes.getLevel())) {
      paragraphs.add(ParagraphDto.from(paragraph));
    }

    paragraph.getChildren().forEach(child -> paragraphs.addAll(collectWithChildren(child, matchingSeniorities)));
    return paragraphs;
  }
}
