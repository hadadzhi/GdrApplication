package ru.cdfe.gdr.domain.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;

public enum Operator {
  EQ, NEQ, GT, GTE, LT, LTE, LIKE, IN;

@JsonCreator
public static Operator fromJsonNode(JsonNode node) {
  if (!node.isTextual()) {
    throw new IllegalArgumentException("operator must be a string");
  }
  final String op = node.asText();
  try {
    return Operator.valueOf(op.replaceAll("[-_ ]", "").toUpperCase());
  } catch (IllegalArgumentException e) {
    throw new IllegalArgumentException(op + " is not a supported operator");
  }
}
}
