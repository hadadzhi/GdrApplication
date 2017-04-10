package ru.cdfe.gdr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.cdfe.gdr.constant.CurveTypes;
import ru.cdfe.gdr.domain.Approximation;
import ru.cdfe.gdr.domain.Curve;
import ru.cdfe.gdr.domain.DataPoint;
import ru.cdfe.gdr.domain.Nucleus;
import ru.cdfe.gdr.domain.Quantity;
import ru.cdfe.gdr.domain.Reaction;
import ru.cdfe.gdr.domain.Record;
import ru.cdfe.gdr.repository.RecordRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Slf4j
@Profile("randomData")
@Component
class RandomDataCreator implements ApplicationRunner {
    private final RecordRepository recordRepository;
    
    public RandomDataCreator(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        recordRepository.deleteAll();
        IntStream.range(0, 1000).parallel().forEach(value -> {
            final Random random = ThreadLocalRandom.current();
            final List<DataPoint> source = new ArrayList<>();
    
            IntStream.range(0, 10).forEach(i -> source.add(
                    new DataPoint(
                            new Quantity(random.nextDouble(), random.nextDouble(), "MeV"),
                            new Quantity(random.nextDouble(), random.nextDouble(), "mb"))));
    
            final List<Approximation> approximations = new ArrayList<>();
    
            IntStream.range(0, 2).forEach(i -> {
                final List<Curve> curves = new ArrayList<>();
        
                IntStream.range(0, 2).forEach(j -> {
                    final Curve c = new Curve();
                    c.setType(random.nextBoolean() ?
                            CurveTypes.GAUSSIAN :
                            CurveTypes.LORENTZIAN);
                    c.setEnergyAtMaxCrossSection(new Quantity(random.nextDouble(), random.nextDouble(), "MeV"));
                    c.setFullWidthAtHalfMaximum(new Quantity(random.nextDouble(), random.nextDouble(), "MeV"));
                    c.setMaxCrossSection(new Quantity(random.nextDouble(), random.nextDouble(), "mb"));
            
                    curves.add(c);
                });
        
                final double chiSquared = random.nextDouble() * 1000;
        
                final Approximation a = new Approximation();
                a.setChiSquared(chiSquared);
                a.setChiSquaredReduced(chiSquared / (source.size() - (curves.size() * 3)));
                a.setDescription("Sample data " + random.nextInt());
                a.setSourceData(source);
                a.setCurves(curves);
        
                approximations.add(a);
            });
    
            final Reaction rn1 = new Reaction();
            rn1.setIncident("A");
            rn1.setOutgoing("B");
            rn1.setTarget(new Nucleus(random.nextInt(100) + 1, random.nextInt(100) + 1));
            rn1.setProduct(new Nucleus(random.nextInt(100) + 1, random.nextInt(100) + 1));
    
            final Reaction rn2 = new Reaction();
            rn2.setIncident("C");
            rn2.setOutgoing("D");
            rn2.setTarget(new Nucleus(random.nextInt(100) + 1, random.nextInt(100) + 1));
            rn2.setProduct(new Nucleus(random.nextInt(100) + 1, random.nextInt(100) + 1));
    
            Record r = new Record();
            r.setEnergyCenter(new Quantity(random.nextDouble(), random.nextDouble(), "MeV"));
            r.setFirstMoment(new Quantity(random.nextDouble(), random.nextDouble(), "mb"));
            r.setIntegratedCrossSection(new Quantity(random.nextDouble(), random.nextDouble(), "MeV*mb"));
            r.setReactions(Arrays.asList(rn1, rn2));
            r.setSourceData(source);
            r.setApproximations(approximations);
            r.setExforNumber(UUID.randomUUID().toString()
                    .toUpperCase().replace("-", "").substring(0, 8));
    
            r = recordRepository.insert(r);
            log.debug("RandomDataCreator: inserted record: {}", r);
        });
    }
}