package com.stanislavgrujic.documentimporter.web.dto;

import com.stanislavgrujic.documentimporter.model.Attributes;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AttributesDto {

  private String semantics;

  private String level;

  private String clarity;

  private String partOf;

  private String sourceLink;

  private String sourceAuthor;

  private String sourceDetails;

  private String timePeriod;

  private String tags;

  public static AttributesDto from(Attributes attributes) {
    if (attributes == null) {
      return null;
    }

    AttributesDtoBuilder builder = new AttributesDtoBuilder();
    return builder
        .semantics(attributes.getSemantics() == null ? null : attributes.getSemantics().name())
        .level(attributes.getLevel() == null ? null : attributes.getLevel().name())
        .clarity(attributes.getClarity()  == null ? null : attributes.getClarity().name())
        .sourceLink(attributes.getSourceLink())
        .sourceAuthor(attributes.getSourceAuthor())
        .sourceDetails(attributes.getSourceDetails())
        .timePeriod(attributes.getTimePeriod())
        .tags(attributes.getTags())
        .build();
  }

}
