package com.stanislavgrujic.documentimporter.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@ToString(exclude = {"parent", "votes"})
@Entity
public class Paragraph {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  @OneToMany(mappedBy = "paragraph")
  private Set<Vote> votes = new HashSet<>();

  public void addChild(Paragraph paragraph) {
    children.add(paragraph);
  }

  public void addChildren(List<Paragraph> paragraphs) {
    children.addAll(paragraphs);
  }
}
