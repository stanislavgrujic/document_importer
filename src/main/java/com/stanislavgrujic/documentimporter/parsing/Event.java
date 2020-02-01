package com.stanislavgrujic.documentimporter.parsing;

import lombok.Value;

@Value
class Event {
  private EventType type;
  private String line;
}
