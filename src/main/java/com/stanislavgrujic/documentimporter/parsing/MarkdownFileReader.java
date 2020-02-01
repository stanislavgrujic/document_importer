package com.stanislavgrujic.documentimporter.parsing;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Component
public class MarkdownFileReader {

  @SneakyThrows
  @PostConstruct
  public void read() {
    ClassPathResource classPathResource = new ClassPathResource("Knowledge blocks - test.md");
    File file = classPathResource.getFile();

    try (FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader)) {
      String line = reader.readLine();

      MarkdownParser mdParser = MarkdownParser.INIT;

      while (line != null) {

        Event event = createEvent(line);
        mdParser = (MarkdownParser) mdParser.handle(event);

        line = reader.readLine();
      }

      mdParser.saveParagraph();
      mdParser.printStructure();
    }

  }

  private Event createEvent(String line) {
    if (isAttributesLine(line)) {
      return new Event(EventType.ATTRIBUTES_READ, line);

    } else if (isTitle(line)) {
      return new Event(EventType.TITLE_READ, line);

    } else {
      return new Event(EventType.TEXT_READ, line);
    }
  }

  private boolean isAttributesLine(String line) {
    return line.startsWith("######");
  }

  private boolean isTitle(String line) {
    return line.startsWith("#") && line.indexOf(" ") < 6;
  }

}
