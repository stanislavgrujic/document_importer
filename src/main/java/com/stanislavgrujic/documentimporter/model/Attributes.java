package com.stanislavgrujic.documentimporter.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Attributes {

  @SerializedName("sem")
  private Semantics semantics;

  @SerializedName("lev")
  private Level level;

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
