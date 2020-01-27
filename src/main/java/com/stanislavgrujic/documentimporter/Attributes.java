package com.stanislavgrujic.documentimporter;

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

}
