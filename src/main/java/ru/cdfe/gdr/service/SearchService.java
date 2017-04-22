package ru.cdfe.gdr.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Set;

/**
 * Generic domain object search service.
 * @param <T> the type of the domain object to search for.
 */
public interface SearchService<T> {
    /**
     * Searches for domain objects matching the specified criteria.
     * @param criteria a map from a field name to an object that describes
     *                 the desired value of that field. Currently, if the
     *                 object is a {@link String}, it is interpreted as a regex,
     *                 all other object types are interpreted as an exact value.
     * @param fields   a set of field names that determines which fields
     *                 in the returned domain object will be initialized.
     *                 If empty, all fields will be initialized.
     *                 If not empty, non-initialized fields in the returned
     *                 domain objects will be {@code null}.
     *                 The {@code id} field is always initialized.
     * @param pageable page size, offset and sorting parameters.
     * @throws IllegalArgumentException if any of the parameters are {@code null}
     */
    Page<T> find(Map<String, Object> criteria, Set<String> fields, Pageable pageable);
}
