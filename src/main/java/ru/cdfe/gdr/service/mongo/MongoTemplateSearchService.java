package ru.cdfe.gdr.service.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;
import ru.cdfe.gdr.service.SearchService;

import java.util.Map;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class MongoTemplateSearchService<T> implements SearchService<T> {
    private final Class<T> domainObjectType;
    private final MongoTemplate mongo;
    
    public MongoTemplateSearchService(Class<T> domainObjectType,
                                      MongoTemplate mongo) {
        
        this.domainObjectType = domainObjectType;
        this.mongo = mongo;
    }
    
    @Override
    public Page<T> find(Map<String, Object> criteria, Set<String> fields, Pageable pageable) {
        Assert.notNull(criteria, "arguments must not be null");
        Assert.notNull(fields, "arguments must not be null");
        Assert.notNull(pageable, "arguments must not be null");
        
        final Query query = new Query();
        criteria.forEach((p, v) -> {
            if (v instanceof String) {
                query.addCriteria(where(p).regex((String) v));
            } else {
                query.addCriteria(where(p).is(v));
            }
        });
        
        final long total = mongo.count(query, domainObjectType);
        
        query.with(pageable);
        fields.forEach(f -> query.fields().include(f));
        
        return new PageImpl<>(mongo.find(query.with(pageable), domainObjectType), pageable, total);
    }
}
