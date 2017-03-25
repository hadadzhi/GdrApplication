package ru.cdfe.gdr.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
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
import ru.cdfe.gdr.exceptions.NoSuchRecordException;
import ru.cdfe.gdr.exceptions.OptimisticLockingException;
import ru.cdfe.gdr.repositories.RecordRepository;
import ru.cdfe.gdr.services.LinkService;

import java.util.Optional;

@RestController
@ExposesResourceFor(Record.class)
@RequestMapping(Relations.REPOSITORY + "/" + Relations.RECORDS)
@Slf4j
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
    public PagedResources<Resource<RecordExcerpt>>
    records(Pageable pageable, PagedResourcesAssembler<Record> assembler) {
        
        final PagedResources<Resource<RecordExcerpt>> resources = assembler.toResource(
                recordRepository.findAll(pageable),
                record -> new Resource<>(new RecordExcerpt(record),
                        entityLinks.linkForSingleResource(record).withRel(Relations.RECORD)));
        
        // #1 Set a proper self link
        resources.getLinks().remove(resources.getLink(Link.REL_SELF));
        resources.getLinks().add(linkService.pageLink(entityLinks.linkFor(Record.class), pageable, Link.REL_SELF));
        
        return resources;
    }
    
    @GetMapping("{id}")
    public Resource<Record> record(@PathVariable String id) {
        final Record record = Optional.ofNullable(recordRepository.findOne(id))
                .orElseThrow(NoSuchRecordException::new);
        return new Resource<>(record, entityLinks.linkForSingleResource(record).withSelfRel());
    }
    
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).REPOSITORY)")
    public void delete(@PathVariable String id) {
        if (!recordRepository.exists(id)) {
            log.debug("DELETE: record {} does not exist", id);
            throw new NoSuchRecordException();
        }
        log.debug("DELETE: {}", id);
        recordRepository.delete(id);
    }
    
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).REPOSITORY)")
    public void put(@PathVariable String id, @RequestBody /*@Validated*/ Record record) {
        final Record existingRecord = recordRepository.findOne(id);
        
        if (existingRecord == null) {
            log.debug("PUT: record {} does not exist", id);
            throw new NoSuchRecordException();
        }
        
        record.setId(existingRecord.getId());
        record.setVersion(existingRecord.getVersion());
        
        try {
            log.debug("PUT: {}", record);
            recordRepository.save(record);
        } catch (OptimisticLockingFailureException e) {
            log.warn("Optimistic locking failure: {}", e.getMessage());
            throw new OptimisticLockingException(e);
        }
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).REPOSITORY)")
    public ResponseEntity post(@RequestBody @Validated Record record) {
        log.debug("POST: {}", record);
        record = recordRepository.insert(record);
        return ResponseEntity.created(entityLinks.linkForSingleResource(record).toUri()).build();
    }
}
