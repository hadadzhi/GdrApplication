package ru.cdfe.gdr;

import com.mongodb.MongoClientOptions;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ru.cdfe.gdr.constants.Constants;
import ru.cdfe.gdr.domain.Approximation;
import ru.cdfe.gdr.domain.Curve;
import ru.cdfe.gdr.domain.DataPoint;
import ru.cdfe.gdr.domain.Nucleus;
import ru.cdfe.gdr.domain.Quantity;
import ru.cdfe.gdr.domain.Reaction;
import ru.cdfe.gdr.domain.Record;
import ru.cdfe.gdr.domain.security.Authority;
import ru.cdfe.gdr.domain.security.User;
import ru.cdfe.gdr.repositories.RecordRepository;
import ru.cdfe.gdr.repositories.UserRepository;
import ru.cdfe.gdr.services.FittingService;

import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

@Slf4j
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        WebMvcAutoConfiguration.class
})
public class GdrApplication {
    public static void main(String[] args) {
        SpringApplication.run(GdrApplication.class, args);
    }
    
    @Bean
    public CurieProvider curieProvider(GdrApplicationProperties conf) {
        return new DefaultCurieProvider(conf.getCurieName(), new UriTemplate(conf.getCurieUrlTemplate()));
    }
    
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
    
    @Bean
    public MongoClientOptions mongoClientOptions() {
        return MongoClientOptions.builder()
                .writeConcern(WriteConcern.MAJORITY)
                .readConcern(ReadConcern.MAJORITY)
                .build();
    }
    
    @Bean
    public ApplicationRunner defaultUserCreator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                final User defaultUser = new User();
                
                defaultUser.setName(Constants.DEFAULT_USER_NAME);
                defaultUser.setSecret(passwordEncoder.encode(Constants.DEFAULT_USER_SECRET));
                defaultUser.setAuthorities(Arrays.stream(Authority.values()).collect(toSet()));
                
                log.info("No users found, creating default user: {}, with password: {}",
                        defaultUser, Constants.DEFAULT_USER_SECRET);
                
                userRepository.insert(defaultUser);
            }
        };
    }
    
    @Bean
    @Profile("randomData")
    public ApplicationRunner randomDataCreator(RecordRepository recordRepository) {
        return args -> {
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
                                FittingService.Curves.GAUSSIAN :
                                FittingService.Curves.LORENTZIAN);
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
                
                final Record r = new Record();
                r.setEnergyCenter(new Quantity(random.nextDouble(), random.nextDouble(), "MeV"));
                r.setFirstMoment(new Quantity(random.nextDouble(), random.nextDouble(), "mb"));
                r.setIntegratedCrossSection(new Quantity(random.nextDouble(), random.nextDouble(), "MeV*mb"));
                r.setReactions(Arrays.asList(rn1, rn2));
                r.setSourceData(source);
                r.setApproximations(approximations);
                r.setExforNumber(UUID.randomUUID().toString()
                        .toUpperCase().replace("-", "").substring(0, 8));
                
                recordRepository.insert(r);
            });
        };
    }
}
