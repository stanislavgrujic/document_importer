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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MarkdownFileReader {

  @SneakyThrows
  @PostConstruct
  public void read() {
    ClassPathResource classPathResource = new ClassPathResource("Architectural styles.md");
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
            return handleAttributesRead(event);
          case TITLE_READ:
            return handleTitleRead(event);
          case TEXT_READ:
            return handleTextRead(event);
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
            return handleAttributesRead(event);
          case TITLE_READ:
            return handleTitleRead(event);
          case TEXT_READ:
            return handleTextRead(event);
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
            return handleAttributesRead(event);
          case TITLE_READ:
            saveParagraph();
            return handleTitleRead(event);
          case TEXT_READ:
            return handleTextRead(event);
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
            return handleAttributesRead(event);
          case TITLE_READ:
            saveParagraph();
            return handleTitleRead(event);
          case TEXT_READ:
            return handleTextRead(event);
          default:
            throw new IllegalArgumentException("Unknown event type");
        }
      }
    };

    private static Gson parser = new GsonBuilder().registerTypeAdapter(Semantics.class,
        new Semantics.SemanticsJsonDeserializer())
                                                  .create();

    private static Paragraph parent = null;
    private static int depth = 0;
    private static int currentLevel = 0;
    private static Attributes attributes;
    private static String title;
    private static StringBuilder builder = new StringBuilder();
    private static boolean firstChild = false;

    private static Map<Integer, List<Paragraph>> paragraphs = new HashMap<>();

    public void saveParagraph() {
      Paragraph paragraph = new Paragraph();
      paragraph.setAttributes(attributes);
      paragraph.setTitle(title);
      paragraph.setValue(builder.toString());

      if (parent == null) {
        parent = paragraph;
        currentLevel = depth;
      } else {
        if (title != null) {
          determineCurrentParagraphPlacing();

          parent.addChild(paragraph);
          paragraph.setParent(parent);
          parent = paragraph;
          currentLevel++;
          firstChild = true;
        } else {
          if (firstChild) {
            currentLevel++;
            firstChild = false;
          }
          parent.addChild(paragraph);
          paragraph.setParent(parent);
        }
      }

      saveParagraph(paragraph);

      attributes = null;
      title = null;
      builder.setLength(0);
    }

    private void determineCurrentParagraphPlacing() {
      if (currentLevel > depth) {
        while (currentLevel > depth) {
          parent = parent.getParent();
          currentLevel--;
        }

      } else {
        while (depth - currentLevel > 1) {
          parent = getLastChildOfCurrentParent();
          currentLevel++;
        }
      }
    }

    private void printStructure() {
      Paragraph topParent = parent;
      while (topParent.getParent() != null) {
        topParent = topParent.getParent();
      }

      printParagraph(topParent, "");
    }

    private static void printParagraph(Paragraph paragraph, String indent) {
      String text = paragraph.getTitle() == null ? paragraph.getValue() : paragraph.getTitle();
      System.out.println(indent + text);
      paragraph.getChildren().forEach(p -> printParagraph(p, (indent + "-")));
    }

    private Paragraph getLastChildOfCurrentParent() {
      return parent.getChildren().get(parent.getChildren().size() - 1);
    }

    private static State handleAttributesRead(Event event) {
      attributes = parseParagraphAttributes(event.getLine(), parser);
      return ATTRIBUTES_READ;
    }

    private static State handleTitleRead(Event event) {
      String line = event.getLine();
      depth = line.indexOf(" ");
      title = line.substring(depth + 1);
      return TITLE_READ;
    }

    private static State handleTextRead(Event event) {
      builder.append(event.getLine());
      return TEXT_READ;
    }

    private static void saveParagraph(Paragraph paragraph) {
      // save paragraph
      System.out.println(paragraph);
    }
  }

  private interface State {
    State handle(Event event);
  }

  @Value
  private static class Event {
    private EventType type;
    private String line;
  }

  private enum EventType {
    ATTRIBUTES_READ,
    TITLE_READ,
    TEXT_READ
  }

}
