package ru.cdfe.gdr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.cdfe.gdr.constant.Parameters;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.controller.ServiceController;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class LinkService {
    private final HateoasPageableHandlerMethodArgumentResolver resolver;
    
    @Autowired
    public LinkService(HateoasPageableHandlerMethodArgumentResolver resolver) {
        
        this.resolver = resolver;
    }
    
    public Link paginatedLink(LinkBuilder linkBuilder, String rel, TemplateVariable... additionalVariables) {
        final URI uri = linkBuilder.toUri();
        final UriTemplate template = new UriTemplate(uri.toString(),
                resolver.getPaginationTemplateVariables(null, UriComponentsBuilder.fromUri(uri).build()));
        return new Link(template.with(new TemplateVariables(additionalVariables)), rel);
    }
    
    public UriComponentsBuilder pageLinkBuilder(LinkBuilder linkBuilder, Pageable page) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUri(linkBuilder.toUri());
        resolver.enhance(builder, null, page);
        return builder;
    }
    
    public Link pageLink(LinkBuilder linkBuilder, Pageable page, String rel) {
        return new Link(new UriTemplate(pageLinkBuilder(linkBuilder, page).build().toString()), rel);
    }
    
    public Link fitterLink() {
        return linkTo(ServiceController.class).slash(Relations.FITTER).withRel(Relations.FITTER);
    }
    
    public Link exforLink() {
        final String uri = linkTo(ServiceController.class).slash(Relations.EXFOR).toUri().toString();
        final UriTemplate template = new UriTemplate(uri)
                .with(Parameters.SUBENT, TemplateVariable.VariableType.REQUEST_PARAM)
                .with(Parameters.ENERGY_COL, TemplateVariable.VariableType.REQUEST_PARAM)
                .with(Parameters.CROSS_SECTION_COL, TemplateVariable.VariableType.REQUEST_PARAM)
                .with(Parameters.CROSS_SECTION_ERR_COL, TemplateVariable.VariableType.REQUEST_PARAM);
        return new Link(template, Relations.EXFOR);
    }
    
    public void fixPaginatedSelfLink(PagedResources<?> pagedResources, Pageable page, LinkBuilder selfLinkBuilder) {
        pagedResources.getLinks().remove(pagedResources.getLink(Link.REL_SELF));
        pagedResources.getLinks().add(pageLink(selfLinkBuilder, page, Link.REL_SELF));
    }
}
