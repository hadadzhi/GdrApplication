package ru.cdfe.gdr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.domain.Record;
import ru.cdfe.gdr.domain.search.SearchQuery;
import ru.cdfe.gdr.service.SearchService;

@Slf4j
@RestController
@RequestMapping(Relations.REPOSITORY)
public class SearchController {
private final SearchService searchService;
private final EntityLinks entityLinks;

@Autowired
public SearchController(SearchService searchService, EntityLinks entityLinks) {
  this.searchService = searchService;
  this.entityLinks = entityLinks;
}

@PostMapping(Relations.RECORDS_SEARCH)
@PreAuthorize("permitAll()")
public PagedResources<Resource<Record>> searchRecords(@RequestBody @Validated SearchQuery query,
                                                      Pageable pageable,
                                                      PagedResourcesAssembler<Record> assembler) {
  return assembler.toResource(search(Record.class, query, pageable),
      entity -> new Resource<>(entity, entityLinks.linkToSingleResource(entity).withRel(Relations.RECORD)));
}

private <T> Page<T> search(Class<T> domainObjectType, SearchQuery query, Pageable pageable) {
  log.debug("Type: {}, Query: {}, Pageable: {}", domainObjectType, query, pageable);
  final Page<T> result = searchService.find(query, pageable, domainObjectType);
  log.debug("Found: {}", result);
  log.trace("Page content: {}", result.getContent());
  return result;
}
}
