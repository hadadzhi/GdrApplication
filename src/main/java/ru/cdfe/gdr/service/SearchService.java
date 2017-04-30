package ru.cdfe.gdr.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.cdfe.gdr.domain.search.SearchQuery;

/**
 * Generic domain object search service.
 */
public interface SearchService {
    /**
     * Searches for domain objects matching the specified query.
     * @param query            the search query.
     * @param pageable         page size, offset and sorting parameters.
     * @param domainObjectType the type of the domain object to search for.
     * @throws IllegalArgumentException if any of the parameters are {@code null}
     */
    <T> Page<T> find(SearchQuery query, Pageable pageable, Class<T> domainObjectType);
}
