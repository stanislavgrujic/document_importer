package com.stanislavgrujic.documentimporter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class VoteKey implements Serializable {

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "paragraph_id")
  private Long paragraphId;
}
