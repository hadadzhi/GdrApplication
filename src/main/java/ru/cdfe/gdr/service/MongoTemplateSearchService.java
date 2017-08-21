package ru.cdfe.gdr.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.cdfe.gdr.domain.search.SearchQuery;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * An implementation of the {@link SearchService} using the {@link MongoTemplate}.
 */
@Slf4j
@Service
public class MongoTemplateSearchService implements SearchService {
private final MongoTemplate mongo;

@Autowired
public MongoTemplateSearchService(MongoTemplate mongo){
	this.mongo = mongo;
}

@Override
public <T> Page<T> find(SearchQuery query, Pageable pageable, Class<T> documentType){
	Assert.notNull(query, "arguments must not be null");
	Assert.notNull(pageable, "arguments must not be null");

	final Query q = new Query();
	final Set<Criteria> terms = new HashSet<>();
	query.getSelect().forEach(f -> q.fields().include(f));
	query.getWhere().forEach((term) ->
	{
	final Criteria cr = where(term.getField());
	switch (term.getOperator())
		{
		case EQ:
			terms.add(cr.is(term.getValue()));
			break;
		case NEQ:
			terms.add(cr.not().is(term.getValue()));
			break;
		case GT:
			terms.add(cr.gt(term.getValue()));
			break;
		case GTE:
			terms.add(cr.gte(term.getValue()));
			break;
		case LT:
			terms.add(cr.lt(term.getValue()));
			break;
		case LTE:
			terms.add(cr.lte(term.getValue()));
			break;
		case LIKE:
			terms.add(cr.regex(term.getValue().toString()));
			break;
		case IN:
			terms.add(cr.in((Collection<?>) term.getValue()));
			break;
		}
	});
	q.addCriteria(new Criteria().andOperator(terms.toArray(new Criteria[] {})));
	q.with(pageable);

	log.debug("Mongo query: {}", q);
	return new PageImpl<>(mongo.find(q, documentType), pageable, mongo.count(q, documentType));
}
}
