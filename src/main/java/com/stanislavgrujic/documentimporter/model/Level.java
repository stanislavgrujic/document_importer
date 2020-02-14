package com.stanislavgrujic.documentimporter.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Level {

  ENTRY,
  MEDIUM,
  ADVANCED;

  public static List<Level> getLevels(String level) {
    if (StringUtils.isEmpty(level)) {
      return Arrays.asList(Level.values());
    }

    Level seniority = Level.valueOf(level);
    switch (seniority) {
      case ENTRY:
        return Collections.singletonList(Level.ENTRY);
      case MEDIUM:
        return Arrays.asList(ENTRY, MEDIUM);
      case ADVANCED:
        return Arrays.asList(Level.values());
      default:
        throw new IllegalArgumentException("Unexpected seniority value: " + level);
    }
  }

  public static Level from(String value) {
    if (value == null) {
      return Level.ENTRY;
    }

    return Level.valueOf(value.toUpperCase());

  }

  public static class LevelJsonDeserializer implements JsonDeserializer<Level> {

    @Override
    public Level deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {
      String value = json.getAsString();
      return Level.from(value);
    }
  }
}
