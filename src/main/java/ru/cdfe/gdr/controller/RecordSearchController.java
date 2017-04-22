package ru.cdfe.gdr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.cdfe.gdr.domain.dto.SearchRequest;
import ru.cdfe.gdr.service.SearchService;

@Slf4j
@RestController
@RequestMapping(Relations.REPOSITORY + "/" + Relations.RECORDS_SEARCH)
@PreAuthorize("permitAll()")
public class RecordSearchController {
    private final SearchService<Record> recordSearchService;
    private final EntityLinks entityLinks;
    
    @Autowired
    public RecordSearchController(SearchService<Record> recordSearchService,
                                  EntityLinks entityLinks) {
        
        this.recordSearchService = recordSearchService;
        this.entityLinks = entityLinks;
    }

    @PostMapping
    @PreAuthorize("permitAll()")
    public PagedResources<Resource<Record>> search(@RequestBody @Validated SearchRequest query,
                                                   Pageable pageable,
                                                   PagedResourcesAssembler<Record> assembler) {
        log.debug("POST: query: {}, page: {}", query, pageable);
        
        final PagedResources<Resource<Record>> resources = assembler.toResource(
                recordSearchService.find(query.getWhere(), query.getSelect(), pageable),
                record -> new Resource<>(record, entityLinks.linkForSingleResource(record).withRel(Relations.RECORD)));
        
        log.debug("POST: found: {}", resources);
        return resources;
    }
}
