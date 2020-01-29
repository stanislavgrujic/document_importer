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
            saveParagraph(paragraph, builder.toString());
            builder.setLength(0);
          }

          paragraph = new Paragraph();

          parseParagraphAttributes(line, paragraph, parser);

          line = reader.readLine();
          continue;
        }

        if (isTitle(line)) {
          int depth = line.indexOf(" ");

          if (paragraph != null) {
            saveParagraph(paragraph, builder.toString());
            builder.setLength(0);
          }

          Paragraph newParagraph = new Paragraph();
          if (depth > currentLevel) {
            newParagraph.setParent(paragraph);

            if (paragraph != null) {
              paragraph.addChild(newParagraph);
            }

            paragraph = newParagraph;
            currentLevel = depth;
          } else if (depth == currentLevel) {
            newParagraph.setParent(paragraph.getParent());
            paragraph.getParent().addChild(newParagraph);
          } else {

            Paragraph ancestor = null;
            while (depth < currentLevel) {
              ancestor = paragraph.getParent();
              currentLevel--;
            }

            ancestor.addChild(newParagraph);
            newParagraph.setParent(ancestor);
          }
        }

        builder.append(line);

        line = reader.readLine();
      }
    }

  }

  private void saveParagraph(Paragraph paragraph, String value) {
    paragraph.setValue(value);
    // todo save paragraph
    System.out.println(paragraph);
  }

  private void parseParagraphAttributes(String line, Paragraph paragraph, Gson parser) {
    String json = line.substring(line.indexOf(" "));
    Attributes attributes = parser.fromJson(json, Attributes.class);
    paragraph.setAttributes(attributes);
  }

  private boolean isAttributesLine(String line) {
    return line.startsWith("######");
  }

  private boolean isTitle(String line) {
    return line.startsWith("#") && line.indexOf(" ") < 5;
  }

}
