package ru.cdfe.gdr.domain.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.core.Relation;
import org.springframework.security.core.GrantedAuthority;
import ru.cdfe.gdr.constant.Relations;

@Getter
@ToString
@Relation(collectionRelation = Relations.AUTHORITIES)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Authority implements GrantedAuthority {
  USERS("Manage users and authorities"),
  RECORDS("Create, delete and edit records"),
  EXFOR("Generate records from exfor"),
  FITTING("Use the fitting service");

private String name;
private String description;

Authority(String description) {
  this.name = name();
  this.description = description;
}

@JsonCreator
public static Authority fromJsonNode(JsonNode node) {
  if (node.has("name")) {
    return valueOf(node.get("name").asText());
  }
  throw new IllegalArgumentException("The node does not represent an authority");
}

@Override
@JsonIgnore
public String getAuthority() {
  return name;
}
}
