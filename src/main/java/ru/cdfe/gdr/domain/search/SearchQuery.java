package ru.cdfe.gdr.domain.search;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Set;

@Data
public final class SearchQuery {
@NotEmpty
private final Set<SearchTerm> where = Collections.emptySet();

@NotNull
private final Set<String> select = Collections.emptySet();
}
