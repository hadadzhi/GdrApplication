package ru.cdfe.gdr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.constant.Relations;
import ru.cdfe.gdr.domain.security.Authority;

import java.util.Arrays;

@RestController
@ExposesResourceFor(Authority.class)
@RequestMapping(Relations.REPOSITORY + "/" + Relations.AUTHORITIES)
@PreAuthorize("hasAuthority(T(ru.cdfe.gdr.domain.security.Authority).USERS)")
class AuthoritiesController {
private final EntityLinks entityLinks;

@Autowired
AuthoritiesController(EntityLinks entityLinks){
	this.entityLinks = entityLinks;
}

@GetMapping
public Resources<Authority> availableAuthorities(){
	return new Resources<>(Arrays.asList(Authority.values()),
			entityLinks.linkFor(Authority.class).withRel(Relations.AUTHORITIES));
}
}
