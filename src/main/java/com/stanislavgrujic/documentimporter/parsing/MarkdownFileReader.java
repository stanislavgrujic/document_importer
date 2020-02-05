package com.stanislavgrujic.documentimporter.parsing;

import com.stanislavgrujic.documentimporter.model.Paragraph;
import com.stanislavgrujic.documentimporter.repository.ParagraphRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class MarkdownFileReader {

  @Autowired
  private ParagraphRepository repository;

  @SneakyThrows
  @PostConstruct
  public void read() {
    ClassPathResource classPathResource = new ClassPathResource("markdown/Architectural_styles.md");

    try (InputStreamReader fileReader = new InputStreamReader(classPathResource.getInputStream());
        BufferedReader reader = new BufferedReader(fileReader)) {
      String line = reader.readLine();

      MarkdownParser mdParser = MarkdownParser.INIT;

      while (line != null) {

        Event event = createEvent(line);
        mdParser = (MarkdownParser) mdParser.handle(event);

        line = reader.readLine();
      }

      mdParser.saveParagraph();
      Paragraph topParagraph = mdParser.getTopParent();
      repository.save(topParagraph);
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
