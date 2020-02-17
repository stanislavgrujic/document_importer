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
import java.util.ArrayList;
import java.util.List;

@Component
public class MarkdownFileReader {

  @Autowired
  private ParagraphRepository repository;

  @SneakyThrows
  @PostConstruct
  public void read() {
    List<ClassPathResource> resources = new ArrayList<>();
    resources.add(new ClassPathResource("markdown/Architectural_styles.md"));
    resources.add(new ClassPathResource("markdown/Data_distribution_and_database_clustering.md"));
    resources.add(new ClassPathResource("markdown/Data_storage.md"));
    resources.add(new ClassPathResource("markdown/Key_system_characteristics.md"));

    Paragraph systemDesign = new Paragraph();
    systemDesign.setTitle("System Design");

    for (ClassPathResource resource : resources) {
      try (InputStreamReader fileReader = new InputStreamReader(resource.getInputStream());
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

        systemDesign.addChildren(topParagraph.getChildren());
        for (Paragraph child : topParagraph.getChildren()) {
          child.setParent(systemDesign);
        }
      }
    }

    repository.save(systemDesign);

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
