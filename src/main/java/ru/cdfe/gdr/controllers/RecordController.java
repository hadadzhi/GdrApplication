package ru.cdfe.gdr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.constants.Relations;
import ru.cdfe.gdr.domain.Record;
import ru.cdfe.gdr.exceptions.NoSuchRecordException;
import ru.cdfe.gdr.repositories.RecordRepository;
import ru.cdfe.gdr.services.PageableLinks;

import java.util.Optional;

@RestController
@ExposesResourceFor(Record.class)
@RequestMapping(Relations.REPOSITORY + "/" + Relations.RECORDS)
public class RecordController {
    private final RecordRepository recordRepository;
    private final EntityLinks entityLinks;
    private final PageableLinks pageableLinks;
    
    @Autowired
    public RecordController(RecordRepository recordRepository,
                            EntityLinks entityLinks,
                            PageableLinks pageableLinks) {
        
        this.recordRepository = recordRepository;
        this.entityLinks = entityLinks;
        this.pageableLinks = pageableLinks;
    }
    
    @GetMapping
    public PagedResources<Resource<Record>> records(Pageable pageable,
                                                    PagedResourcesAssembler<Record> assembler) {
        
        final PagedResources<Resource<Record>> resources = assembler.toResource(recordRepository.findAll(pageable),
                record -> new Resource<>(record, entityLinks.linkForSingleResource(record).withSelfRel()));
        
        // Set a proper self link
        resources.getLinks().remove(resources.getLink(Link.REL_SELF));
        resources.getLinks().add(pageableLinks.pageLink(entityLinks.linkFor(Record.class), pageable, Link.REL_SELF));
        
        return resources;
    }
    
    @GetMapping("{id}")
    public Resource<Record> record(@PathVariable String id) {
        final Record record = Optional.ofNullable(recordRepository.findOne(id))
                .orElseThrow(NoSuchRecordException::new);
        return new Resource<>(record, entityLinks.linkForSingleResource(record).withSelfRel());
    }
}
