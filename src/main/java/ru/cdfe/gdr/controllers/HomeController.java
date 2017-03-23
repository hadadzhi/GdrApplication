package ru.cdfe.gdr.controllers;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.constants.Relations;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public ResourceSupport links() {
        final ResourceSupport links = new ResourceSupport();
        
        links.add(linkTo(methodOn(HomeController.class).links()).withSelfRel());
        links.add(linkTo(HomeController.class).slash(Relations.REPOSITORY).withRel(Relations.REPOSITORY));
        links.add(linkTo(methodOn(AuthenticationController.class).login(null, null)).withRel(Relations.LOGIN));
        links.add(linkTo(methodOn(AuthenticationController.class).logout(null)).withRel(Relations.LOGOUT));
        
        return links;
    }
}
