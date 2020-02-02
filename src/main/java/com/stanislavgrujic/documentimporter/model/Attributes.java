package com.stanislavgrujic.documentimporter.model;

import com.google.gson.annotations.SerializedName;
import lombok.Value;

@Value
public class Attributes {

   @SerializedName(value="sem")
   private Semantics semantics;

   @SerializedName(value="lev")
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
