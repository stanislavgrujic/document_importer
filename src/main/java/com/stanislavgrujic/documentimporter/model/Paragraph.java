package com.stanislavgrujic.documentimporter.model;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = {"parent"})
public class Paragraph {

  private Attributes attributes;

  private String title;

  private String value;

  private Paragraph parent;

  private List<Paragraph> children = new ArrayList<>();

  public void addChild(Paragraph paragraph) {
    children.add(paragraph);
  }
}
