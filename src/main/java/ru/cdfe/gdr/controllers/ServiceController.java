package ru.cdfe.gdr.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.cdfe.gdr.GdrParameters;
import ru.cdfe.gdr.constants.Parameters;
import ru.cdfe.gdr.constants.Relations;
import ru.cdfe.gdr.domain.Approximation;
import ru.cdfe.gdr.domain.DataPoint;
import ru.cdfe.gdr.domain.Reaction;
import ru.cdfe.gdr.domain.Record;
import ru.cdfe.gdr.services.ExforService;
import ru.cdfe.gdr.services.FittingService;
import ru.cdfe.gdr.services.LinkService;

import java.util.List;

@RestController
@Slf4j
public class ServiceController {
    private final ExforService exforService;
    private final FittingService fittingService;
    private final LinkService linkService;
    
    @Autowired
    public ServiceController(ExforService exforService, FittingService fittingService, LinkService linkService) {
        this.exforService = exforService;
        this.fittingService = fittingService;
        this.linkService = linkService;
    }
    
    @GetMapping(Relations.NEW_RECORD)
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).EXFOR)")
    public Resource<Record>
    newRecord(@RequestParam(name = Parameters.EXFOR_NUMBER) String exforNumber,
              @RequestParam(name = Parameters.ENERGY_COL, defaultValue = "0") int enCol,
              @RequestParam(name = Parameters.CROSS_SECTION_COL, defaultValue = "1") int csCol,
              @RequestParam(name = Parameters.CROSS_SECTION_ERR_COL, defaultValue = "2") int csErrCol) {
        
        final List<DataPoint> exforData = exforService.getData(exforNumber, enCol, csCol, csErrCol);
        final List<Reaction> exforReactions = exforService.getReactions(exforNumber);
        
        final GdrParameters parameters = new GdrParameters(exforData);
        
        final Record recordTemplate = new Record();
        recordTemplate.setExforNumber(exforNumber);
        recordTemplate.setReactions(exforReactions);
        recordTemplate.setSourceData(exforData);
        recordTemplate.setIntegratedCrossSection(parameters.getIntegratedCrossSection());
        recordTemplate.setEnergyCenter(parameters.getEnergyCenter());
        recordTemplate.setFirstMoment(parameters.getFirstMoment());
        
        return new Resource<>(recordTemplate, linkService.fitterLink());
    }
    
    @PostMapping(Relations.FITTER)
    @PreAuthorize("hasAuthority(T(ru.cdfe.gdr.constants.Authorities).FITTING)")
    public Resource<Approximation> fit(@RequestBody @Validated Approximation initGuess) {
        fittingService.fit(initGuess);
        return new Resource<>(initGuess);
    }
}