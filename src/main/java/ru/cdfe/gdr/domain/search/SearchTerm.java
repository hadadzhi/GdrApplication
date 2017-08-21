package ru.cdfe.gdr.domain.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collection;

@Data
public final class SearchTerm {
private final String field;
private final Operator operator;
private final Object value;

@JsonCreator
public SearchTerm(@JsonProperty("field") String field,
                  @JsonProperty("operator") Operator operator,
                  @JsonProperty("value") Object value){

	if (operator.equals(Operator.LIKE) && !(value instanceof String))
		{
		throw new IllegalArgumentException("Operator LIKE requires a string value");
		}
	if (operator.equals(Operator.IN) && !(value instanceof Collection))
		{
		throw new IllegalArgumentException("Operator IN requires a collection value");
		}

	this.field = field;
	this.operator = operator;
	this.value = value;
}
}
