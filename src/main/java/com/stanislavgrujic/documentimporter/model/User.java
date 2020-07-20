package com.stanislavgrujic.documentimporter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString(exclude = "votes")
@EqualsAndHashCode(exclude = "votes")
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotNull
  private String email;

  private String fullName;

  @Enumerated(value = EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  private Set<Vote> votes = new HashSet<>();
}
