package ru.cdfe.gdr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.constants.Relations;
import ru.cdfe.gdr.services.LinkService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/")
public class HomeController {
    private final LinkService linkService;
    
    @Autowired
    public HomeController(LinkService linkService) {
        this.linkService = linkService;
    }
    
    @GetMapping
    public ResourceSupport links() {
        final ResourceSupport links = new ResourceSupport();
        links.add(linkTo(HomeController.class).withSelfRel());
        links.add(linkTo(HomeController.class).slash(Relations.REPOSITORY).withRel(Relations.REPOSITORY));
        links.add(linkTo(AuthenticationController.class).slash(Relations.LOGIN).withRel(Relations.LOGIN));
        links.add(linkTo(AuthenticationController.class).slash(Relations.LOGOUT).withRel(Relations.LOGOUT));
        links.add(linkService.newRecordLink());
        links.add(linkService.fitterLink());
        return links;
    }
}
