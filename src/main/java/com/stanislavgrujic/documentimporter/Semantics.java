package com.stanislavgrujic.documentimporter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public enum Semantics {

  WHY("why"),
  DEFINITION("def"),
  EXPLANATION("expl"),
  EXAMPLE("exampl"),
  METAPHOR("meth"),
  COMPOSED_OF("comp-of"),
  TYPES("types"),
  USE_CASES("use-case"),
  WARNING("war"),
  METRICS("num"),
  IMPORTANT("rem"),
  SIMILAR_TO("simil"),
  DIFFERENT_FROM("diff"),
  BEST_PRACTICE("pattern"),
  ANTI_PATTERN("untipat"),
  CORRELATION("corr");

  private String value;

  Semantics(String value) {
    this.value = value;
  }

  public static Semantics from(String value) {
    if (value == null) {
      return Semantics.EXPLANATION;
    }

    for (Semantics semantics : Semantics.values()) {
      if (semantics.value.equals(value)) {
        return semantics;
      }
    }

    throw new IllegalArgumentException("Unknown value: " + value);
  }

  public static class SemanticsJsonDeserializer implements JsonDeserializer<Semantics> {

    @Override
    public Semantics deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      String value = json.getAsString();
      return Semantics.from(value);
    }
  }
}
