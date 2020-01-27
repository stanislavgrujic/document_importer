package com.stanislavgrujic.documentimporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    ClassPathResource classPathResource = new ClassPathResource("Knowledge Blocks - test.md");
    File file = classPathResource.getFile();

    try (FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader)) {
      Gson parser = new GsonBuilder().registerTypeAdapter(Semantics.class, new Semantics.SemanticsJsonDeserializer())
                                     .create();

      String line = reader.readLine();
      StringBuilder builder = new StringBuilder();
      Paragraph paragraph = null;
      Paragraph parent = null;
      int currentLevel = 0;
      while (line != null) {

        if (isAttributesLine(line)) {

          if (paragraph != null) {
            paragraph.setValue(builder.toString());
            // todo save paragraph
            System.out.println(paragraph);
          }

          paragraph = new Paragraph();

          parseParagraphAttributes(line, paragraph, parser);

          builder.setLength(0);
          line = reader.readLine();
          continue;
        }

        builder.append(line);

        line = reader.readLine();
      }
    }

  }

  private void parseParagraphAttributes(String line, Paragraph paragraph, Gson parser) {
    String json = line.substring(line.indexOf(" "));
    Attributes attributes = parser.fromJson(json, Attributes.class);
    paragraph.setAttributes(attributes);
  }

  private boolean isAttributesLine(String line) {
    return line.startsWith("######");
  }

}
