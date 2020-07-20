package com.stanislavgrujic.documentimporter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Data
@EqualsAndHashCode(exclude = "vote")
@Entity
public class Vote {

  @EmbeddedId
  private VoteKey id;

  @ManyToOne
  @MapsId("user_id")
  private User user;

  @ManyToOne
  @MapsId("paragraph_id")
  private Paragraph paragraph;

  private int vote;
}
