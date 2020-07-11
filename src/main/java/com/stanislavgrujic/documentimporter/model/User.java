package com.stanislavgrujic.documentimporter.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class User {

  @Id
  @GeneratedValue
  private long id;

  @NotNull
  private String email;

  private String fullName;

  @Enumerated(value = EnumType.STRING)
  private Role role;
}
