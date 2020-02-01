package com.stanislavgrujic.documentimporter.parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stanislavgrujic.documentimporter.model.Attributes;
import com.stanislavgrujic.documentimporter.model.Paragraph;
import com.stanislavgrujic.documentimporter.model.Semantics;

import java.util.HashMap;
import java.util.Map;

enum MarkdownParser implements State {
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
  private static Attributes attributes;
  private static String title;
  private static StringBuilder builder = new StringBuilder();

  private static Map<Integer, Paragraph> paragraphs = new HashMap<>();

  public void saveParagraph() {
    Paragraph paragraph = new Paragraph();
    paragraph.setAttributes(attributes);
    paragraph.setTitle(title);
    paragraph.setValue(builder.toString());

    if (title != null) {
      determineCurrentParagraphPlacing(paragraph);

      parent = paragraph;
    } else {
      parent.addChild(paragraph);
      paragraph.setParent(parent);
    }

    saveParagraph(paragraph);

    attributes = null;
    title = null;
    builder.setLength(0);
  }

  private void determineCurrentParagraphPlacing(Paragraph paragraph) {
    paragraphs.put(depth, paragraph);
    Paragraph parent = paragraphs.getOrDefault(depth - 1, new Paragraph());

    parent.addChild(paragraph);
    paragraph.setParent(parent);
  }

  public void printStructure() {
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

  private static State handleAttributesRead(Event event) {
    attributes = parseParagraphAttributes(event.getLine(), parser);
    return ATTRIBUTES_READ;
  }

  private static Attributes parseParagraphAttributes(String line, Gson parser) {
    String json = line.substring(line.indexOf(" "));
    return parser.fromJson(json, Attributes.class);
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
