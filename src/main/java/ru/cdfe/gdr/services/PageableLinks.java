package ru.cdfe.gdr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class PageableLinks {
    private final HateoasPageableHandlerMethodArgumentResolver resolver;
    
    @Autowired
    public PageableLinks(HateoasPageableHandlerMethodArgumentResolver resolver) {
        this.resolver = resolver;
    }
    
    public Link pageableLink(LinkBuilder linkBuilder, String rel) {
        final URI uri = linkBuilder.toUri();
        final UriTemplate template = new UriTemplate(uri.toString(),
                resolver.getPaginationTemplateVariables(null, UriComponentsBuilder.fromUri(uri).build()));
        return new Link(template, rel);
    }
    
    public Link pageLink(LinkBuilder linkBuilder, Pageable page, String rel) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUri(linkBuilder.toUri());
        resolver.enhance(builder, null, page);
        return new Link(new UriTemplate(builder.build().toString()), rel);
    }
}
