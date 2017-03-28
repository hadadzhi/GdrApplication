package ru.cdfe.gdr.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.constants.Relations;
import ru.cdfe.gdr.domain.Record;
import ru.cdfe.gdr.domain.dto.RecordExcerpt;
import ru.cdfe.gdr.exceptions.NotFoundException;
import ru.cdfe.gdr.exceptions.OptimisticLockingException;
import ru.cdfe.gdr.repositories.RecordRepository;
import ru.cdfe.gdr.services.LinkService;

import java.util.Optional;

@Slf4j
@RestController
@ExposesResourceFor(Record.class)
@RequestMapping(Relations.REPOSITORY + "/" + Relations.RECORDS)
@PreAuthorize("hasAuthority(T(ru.cdfe.gdr.domain.security.Authority).RECORDS)")
public class RecordController {
    private final RecordRepository recordRepository;
    private final EntityLinks entityLinks;
    private final LinkService linkService;
    
    @Autowired
    public RecordController(RecordRepository recordRepository,
                            EntityLinks entityLinks,
                            LinkService linkService) {
        
        this.recordRepository = recordRepository;
        this.entityLinks = entityLinks;
        this.linkService = linkService;
    }
    
    @GetMapping
    @PreAuthorize("permitAll()")
    public PagedResources<Resource<RecordExcerpt>>
    getAll(Pageable pageable, PagedResourcesAssembler<Record> assembler) {
        log.debug("GET: getting all records");
        
        final PagedResources<Resource<RecordExcerpt>> resources = assembler.toResource(
                recordRepository.findAll(pageable),
                record -> new Resource<>(new RecordExcerpt(record),
                        entityLinks.linkForSingleResource(record).withRel(Relations.RECORD)));
        
        linkService.fixSelfLink(resources, pageable, entityLinks.linkFor(Record.class));
        return resources;
    }
    
    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Resource<Record> get(@PathVariable String id) {
        log.debug("GET: getting record: {}", id);
        final Record record = Optional.ofNullable(recordRepository.findOne(id))
                .orElseThrow(NotFoundException::new);
        return new Resource<>(record, entityLinks.linkForSingleResource(record).withSelfRel());
    }
    
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        if (!recordRepository.exists(id)) {
            log.debug("DELETE: record not found: {}", id);
            throw new NotFoundException();
        }
        recordRepository.delete(id);
        log.debug("DELETE: successful: {}", id);
    }
    
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@PathVariable String id, @RequestBody @Validated Record record) {
        final Record existingRecord = recordRepository.findOne(id);
        
        if (existingRecord == null) {
            log.debug("PUT: record does not exist: {}", id);
            throw new NotFoundException();
        }
        
        record.setId(existingRecord.getId());
        record.setVersion(existingRecord.getVersion());
        
        try {
            log.debug("PUT: saving record: {}", record);
            record = recordRepository.save(record);
            log.debug("PUT: saved record:  {}", record);
        } catch (OptimisticLockingFailureException e) {
            log.debug("PUT: optimistic locking failure: ", e);
            throw new OptimisticLockingException();
        }
    }
    
    @PostMapping
    public ResponseEntity post(@RequestBody @Validated Record record) {
        log.debug("POST: inserting record: {}", record);
        record = recordRepository.insert(record);
        log.debug("POST: inserted record:  {}", record);
        return ResponseEntity.created(entityLinks.linkForSingleResource(record).toUri()).build();
    }
}
