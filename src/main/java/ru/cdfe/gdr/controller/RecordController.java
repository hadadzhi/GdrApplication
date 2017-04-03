package ru.cdfe.gdr.controller;

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
import org.springframework.hateoas.UriTemplate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import ru.cdfe.gdr.constant.Parameters;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.domain.Record;
import ru.cdfe.gdr.domain.dto.RecordExcerpt;
import ru.cdfe.gdr.exception.OptimisticLockingException;
import ru.cdfe.gdr.exception.RecordNotFound;
import ru.cdfe.gdr.repository.RecordRepository;
import ru.cdfe.gdr.service.LinkService;

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
    getAll(@RequestParam(name = Parameters.SUBENT, required = false) String subent,
           Pageable pageable,
           PagedResourcesAssembler<Record> assembler) {
        
        log.debug(subent != null ? "GET: records for subent " + subent : "GET: all records");
        
        final PagedResources<Resource<RecordExcerpt>> resources = assembler.toResource(
                subent != null ?
                        recordRepository.findByExforNumber(subent, pageable) :
                        recordRepository.findAll(pageable),
                record -> new Resource<>(new RecordExcerpt(record),
                        entityLinks.linkForSingleResource(record).withRel(Relations.RECORD)));
        
        final UriComponentsBuilder builder = linkService.pageLinkBuilder(entityLinks.linkFor(Record.class), pageable);
        
        if (subent != null) {
            builder.queryParam(Parameters.SUBENT, subent);
        }
        
        resources.getLinks().remove(resources.getLink(Link.REL_SELF));
        resources.getLinks().add(new Link(new UriTemplate(builder.toUriString()), Link.REL_SELF));
        
        return resources;
    }
    
    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Resource<Record> get(@PathVariable String id) {
        log.debug("GET: record: {}", id);
        final Record record = Optional.ofNullable(recordRepository.findOne(id)).orElseThrow(RecordNotFound::new);
        return new Resource<>(record, entityLinks.linkForSingleResource(record).withSelfRel());
    }
    
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        if (!recordRepository.exists(id)) {
            log.debug("DELETE: record not found: {}", id);
            throw new RecordNotFound();
        }
        recordRepository.delete(id);
        log.debug("DELETE: deleted record: {}", id);
    }
    
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@PathVariable String id,
                    @RequestBody @Validated Record record) {
        
        final Record existingRecord = recordRepository.findOne(id);
        
        if (existingRecord == null) {
            log.debug("PUT: record not found: {}", id);
            throw new RecordNotFound();
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
