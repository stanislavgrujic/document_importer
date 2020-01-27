package com.stanislavgrujic.documentimporter;

import lombok.Data;

import java.util.List;

@Data
public class Paragraph {

  private Attributes attributes;

  private String value;

  private Paragraph parent;

  private List<Paragraph> children;
}
