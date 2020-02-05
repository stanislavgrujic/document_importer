package com.stanislavgrujic.documentimporter.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Embeddable
public class Attributes {

  @SerializedName("sem")
  @Enumerated(EnumType.STRING)
  private Semantics semantics;

  @SerializedName("lev")
  @Enumerated(EnumType.STRING)
  private Level level = Level.ENTRY;

  @Enumerated(EnumType.STRING)
  private Clarity clarity;

  @SerializedName("part-of")
  private String partOf;

  @SerializedName("source-link")
  private String sourceLink;

  @SerializedName("source-author")
  private String sourceAuthor;

  @SerializedName("source-details")
  private String sourceDetails;

  @SerializedName("time-period")
  private String timePeriod;

  @SerializedName("tags")
  private String tags;

}
