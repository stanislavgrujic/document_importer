package com.stanislavgrujic.documentimporter.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = {"parent"})
@Entity
public class Paragraph {

  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  private Attributes attributes;

  private String title;

  @Lob
  private String value;

  @ManyToOne
  private Paragraph parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST)
  private List<Paragraph> children = new ArrayList<>();

  public void addChild(Paragraph paragraph) {
    children.add(paragraph);
  }

  public void addChildren(List<Paragraph> paragraphs) {
    children.addAll(paragraphs);
  }
}
