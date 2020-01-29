package com.stanislavgrujic.documentimporter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import lombok.Value;
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
      String line = reader.readLine();

      MarkdownParser mdParser = MarkdownParser.INIT;

      while (line != null) {

        Event event;
        if (isAttributesLine(line)) {

          event = new Event(EventType.ATTRIBUTES_READ, line);

        } else if (isTitle(line)) {

          event = new Event(EventType.TITLE_READ, line);

        } else {
          event = new Event(EventType.TEXT_READ, line);
        }

        mdParser = (MarkdownParser) mdParser.handle(event);

        line = reader.readLine();
      }

      mdParser.saveParagraph();
    }

  }

  private static Attributes parseParagraphAttributes(String line, Gson parser) {
    String json = line.substring(line.indexOf(" "));
    return parser.fromJson(json, Attributes.class);
  }

  private boolean isAttributesLine(String line) {
    return line.startsWith("######");
  }

  private boolean isTitle(String line) {
    return line.startsWith("#") && line.indexOf(" ") < 6;
  }

  private enum MarkdownParser implements State {
    INIT {
      @Override
      public State handle(Event event) {
        switch (event.getType()) {
          case ATTRIBUTES_READ:
            attributes = parseParagraphAttributes(event.getLine(), parser);
            return ATTRIBUTES_READ;
          case TITLE_READ:
            String line = event.getLine();
            depth = line.indexOf(" ");
            title = line.substring(depth + 1);
            return TITLE_READ;
          case TEXT_READ:
            builder.append(event.getLine());
            return TEXT_READ;
          default:
            throw new IllegalArgumentException("Unknown event type");
        }
      }
    },

    ATTRIBUTES_READ {
      @Override
      public State handle(Event event) {
        switch (event.getType()) {
          case ATTRIBUTES_READ:
            attributes = parseParagraphAttributes(event.getLine(), parser);
            return ATTRIBUTES_READ;
          case TITLE_READ:
            String line = event.getLine();
            depth = line.indexOf(" ");
            title = line.substring(depth + 1);
            return TITLE_READ;
          case TEXT_READ:
            builder.append(event.getLine());
            return TEXT_READ;
          default:
            throw new IllegalArgumentException("Unknown event type");
        }
      }
    },
    TITLE_READ {
      @Override
      public State handle(Event event) {
        switch (event.getType()) {
          case ATTRIBUTES_READ:
            saveParagraph();
            attributes = parseParagraphAttributes(event.getLine(), parser);
            return ATTRIBUTES_READ;
          case TITLE_READ:
            saveParagraph();
            String line = event.getLine();
            depth = line.indexOf(" ");
            title = line.substring(depth + 1);
            return TITLE_READ;
          case TEXT_READ:
            builder.append(event.getLine());
            return TEXT_READ;
          default:
            throw new IllegalArgumentException("Unknown event type");
        }
      }
    },

    TEXT_READ {
      @Override
      public State handle(Event event) {
        switch (event.getType()) {
          case ATTRIBUTES_READ:
            saveParagraph();
            attributes = parseParagraphAttributes(event.getLine(), parser);
            return ATTRIBUTES_READ;
          case TITLE_READ:
            saveParagraph();
            String line = event.getLine();
            depth = line.indexOf(" ");
            title = line.substring(depth + 1);
            return TITLE_READ;
          case TEXT_READ:
            builder.append(event.getLine());
            return TEXT_READ;
          default:
            throw new IllegalArgumentException("Unknown event type");
        }
      }
    };

    private static Gson parser = new GsonBuilder().registerTypeAdapter(Semantics.class,
        new Semantics.SemanticsJsonDeserializer())
                                                  .create();

    private static int depth = 0;
    private static Attributes attributes;
    private static String title;
    private static StringBuilder builder = new StringBuilder();

    public void saveParagraph() {
      Paragraph paragraph = new Paragraph();
      paragraph.setAttributes(attributes);
      paragraph.setTitle(title);
      paragraph.setValue(builder.toString());

      saveParagraph(paragraph);

      attributes = null;
      title = null;
      builder.setLength(0);
    }

    private static void saveParagraph(Paragraph paragraph) {
      if (paragraph == null) {
        return;
      }
      // todo save paragraph
      System.out.println(paragraph);
    }
  }

  private interface State {
    State handle(Event event);
  }

  @Value
  private class Event {
    private EventType type;
    private String line;
  }

  private enum EventType {
    ATTRIBUTES_READ,
    TITLE_READ,
    TEXT_READ
  }

}
