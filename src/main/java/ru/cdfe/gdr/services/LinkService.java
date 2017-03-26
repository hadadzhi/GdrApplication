package ru.cdfe.gdr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.cdfe.gdr.constants.Parameters;
import ru.cdfe.gdr.constants.Relations;
import ru.cdfe.gdr.controllers.ServiceController;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class LinkService {
    private final HateoasPageableHandlerMethodArgumentResolver resolver;
    private final EntityLinks entityLinks;
    
    @Autowired
    public LinkService(HateoasPageableHandlerMethodArgumentResolver resolver, EntityLinks entityLinks) {
        this.resolver = resolver;
        this.entityLinks = entityLinks;
    }
    
    public Link paginatedLink(LinkBuilder linkBuilder, String rel) {
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
    
    public Link fitterLink() {
        return linkTo(methodOn(ServiceController.class).fit(null)).withRel(Relations.FITTER);
    }
    
    public Link exforLink() {
        final String uri = linkTo(ServiceController.class).slash(Relations.EXFOR).toUri().toString();
        final UriTemplate template = new UriTemplate(uri)
                .with(Parameters.EXFOR_NUMBER, TemplateVariable.VariableType.REQUEST_PARAM)
                .with(Parameters.ENERGY_COL, TemplateVariable.VariableType.REQUEST_PARAM)
                .with(Parameters.CROSS_SECTION_COL, TemplateVariable.VariableType.REQUEST_PARAM)
                .with(Parameters.CROSS_SECTION_ERR_COL, TemplateVariable.VariableType.REQUEST_PARAM);
        return new Link(template, Relations.EXFOR);
    }
    
    public <T> void fixSelfLink(PagedResources<Resource<T>> pagedResources, Pageable page, Class<T> entityType) {
        pagedResources.getLinks().remove(pagedResources.getLink(Link.REL_SELF));
        pagedResources.getLinks().add(pageLink(entityLinks.linkFor(entityType), page, Link.REL_SELF));
    }
}
