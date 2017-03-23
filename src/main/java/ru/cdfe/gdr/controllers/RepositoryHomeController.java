package ru.cdfe.gdr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.constants.Relations;
import ru.cdfe.gdr.domain.Record;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(Relations.REPOSITORY)
public class RepositoryHomeController {
    private final EntityLinks entityLinks;
    
    @Autowired
    public RepositoryHomeController(EntityLinks entityLinks) {
        this.entityLinks = entityLinks;
    }
    
    @GetMapping
    public ResourceSupport repositoryHome() {
        final ResourceSupport links = new ResourceSupport();
        links.add(linkTo(methodOn(RepositoryHomeController.class).repositoryHome()).withSelfRel());
        links.add(entityLinks.linkFor(Record.class).withRel(Relations.RECORDS));
        return links;
    }
}
