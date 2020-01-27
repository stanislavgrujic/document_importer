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
      Gson parser = new GsonBuilder().registerTypeAdapter(Semantics.class, new Semantics.SemanticsJsonDeserializer()).create();

      String line = reader.readLine();
      Attributes attributes;
      StringBuilder builder = new StringBuilder();
      Paragraph paragraph = null;
      while (line != null) {
        if (line.startsWith("######")) {

          if (paragraph != null) {
            paragraph.setValue(builder.toString());
            builder.setLength(0);
            // todo save paragraph
            System.out.println(paragraph);
          }

          paragraph = new Paragraph();

          if (line.endsWith("}")) {
            line = line.substring(line.indexOf(" "));
            builder.append(line);
            attributes = parser.fromJson(builder.toString(), Attributes.class);
            paragraph.setAttributes(attributes);

            line = "";
            builder.setLength(0);
          } else {
            builder.append(line);
          }
        } else {

          if (builder.length() > 0) {
            if (line.endsWith("}")) {
              builder.append(line);

              attributes = parser.fromJson(builder.toString(), Attributes.class);
              paragraph.setAttributes(attributes);

              line = "";
              builder.setLength(0);
            }
          }
        }

        builder.append(line);

        line = reader.readLine();
      }
    }

  }
}
