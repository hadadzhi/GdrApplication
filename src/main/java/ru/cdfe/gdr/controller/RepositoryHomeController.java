package ru.cdfe.gdr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.domain.Record;
import ru.cdfe.gdr.domain.security.Authority;
import ru.cdfe.gdr.domain.security.User;
import ru.cdfe.gdr.service.LinkService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping(Relations.REPOSITORY)
public class RepositoryHomeController {
private final EntityLinks entityLinks;
private final LinkService linkService;

@Autowired
public RepositoryHomeController(EntityLinks entityLinks, LinkService linkService) {
  this.entityLinks = entityLinks;
  this.linkService = linkService;
}

@GetMapping
public ResourceSupport repositoryHome(@AuthenticationPrincipal User user) {
  final ResourceSupport links = new ResourceSupport();

  links.add(linkTo(RepositoryHomeController.class).withSelfRel());

  links.add(linkService.paginatedLink(entityLinks.linkFor(Record.class), Relations.RECORDS));

  links.add(linkService.paginatedLink(linkTo(SearchController.class).slash(Relations.RECORDS_SEARCH),
      Relations.RECORDS_SEARCH));

  if (user != null && user.getAuthorities().contains(Authority.USERS)) {
    links.add(linkService.paginatedLink(entityLinks.linkFor(User.class), Relations.USERS));
    links.add(entityLinks.linkFor(Authority.class).withRel(Relations.AUTHORITIES));
  }

  return links;
}
}
