package ru.cdfe.gdr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.domain.security.Authority;
import ru.cdfe.gdr.domain.security.User;
import ru.cdfe.gdr.service.LinkService;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping
public class HomeController {
    private final LinkService linkService;
    
    @Autowired
    public HomeController(LinkService linkService) {
        this.linkService = linkService;
    }
    
    @GetMapping
    public ResourceSupport home(@AuthenticationPrincipal User user) {
        final ResourceSupport links = new ResourceSupport();
        
        links.add(linkTo(HomeController.class).withSelfRel());
        links.add(linkTo(HomeController.class).slash(Relations.REPOSITORY).withRel(Relations.REPOSITORY));
        
        if (user == null) {
            links.add(linkTo(AuthenticationController.class).slash(Relations.LOGIN).withRel(Relations.LOGIN));
        } else {
            links.add(linkTo(AuthenticationController.class).slash(Relations.LOGOUT).withRel(Relations.LOGOUT));
            links.add(linkTo(AuthenticationController.class).slash(Relations.CURRENT_USER)
                    .withRel(Relations.CURRENT_USER));
            
            if (user.getAuthorities().contains(Authority.EXFOR)) {
                links.add(linkService.exforLink());
            }
            
            if (user.getAuthorities().contains(Authority.FITTING)) {
                links.add(linkService.fitterLink());
            }
        }
        
        return links;
    }
}
