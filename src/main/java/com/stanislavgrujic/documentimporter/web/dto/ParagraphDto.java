package com.stanislavgrujic.documentimporter.web.dto;

import com.stanislavgrujic.documentimporter.model.Paragraph;
import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Value
@Builder
public class ParagraphDto {

  private Long id;

  private String text;

  private List<PathDto> path;

  private AttributesDto attributes;

  public static ParagraphDto from(Paragraph paragraph) {
    ParagraphDtoBuilder builder = new ParagraphDtoBuilder();
    return builder
        .id(paragraph.getId())
        .text(createText(paragraph))
        .attributes(AttributesDto.from(paragraph.getAttributes()))
        .path(createPath(paragraph))
        .build();
  }

  private static String createText(Paragraph paragraph) {
    String title = StringUtils.isNotEmpty(paragraph.getTitle()) ? paragraph.getTitle() + System.lineSeparator() : "";
    String text = StringUtils.isNotEmpty(paragraph.getValue()) ? paragraph.getValue() : "";
    return  title + text;
  }

  private static List<PathDto> createPath(Paragraph paragraph) {
    List<PathDto> path = new ArrayList<>();

    Paragraph parent = paragraph.getParent();
    while (parent != null) {
      PathDto pathDto = new PathDto(parent.getId(), parent.getTitle());
      path.add(pathDto);
      parent = parent.getParent();
    }

    Collections.reverse(path);
    return path;
  }

}
