package ru.cdfe.gdr;

import com.mongodb.MongoClientOptions;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ru.cdfe.gdr.constants.Authorities;
import ru.cdfe.gdr.domain.Approximation;
import ru.cdfe.gdr.domain.Curve;
import ru.cdfe.gdr.domain.DataPoint;
import ru.cdfe.gdr.domain.Nucleus;
import ru.cdfe.gdr.domain.Quantity;
import ru.cdfe.gdr.domain.Reaction;
import ru.cdfe.gdr.domain.Record;
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
import java.util.stream.IntStream;

import static java.util.Collections.singleton;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
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
    @Profile("initUsers")
    public ApplicationRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            try {
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken("admin", "admin",
                                singleton(new SimpleGrantedAuthority(Authorities.ADMIN))));
                
                userRepository.deleteAll();
                userRepository.insert(new User("admin",
                        passwordEncoder.encode("admin"),
                        singleton(Authorities.ADMIN)));
            } finally {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        };
    }
    
    @Bean
    @Profile("randomData")
    public ApplicationRunner createRandomData(RecordRepository repo) {
        return args -> {
            try {
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken("admin", "admin",
                                singleton(new SimpleGrantedAuthority(Authorities.ADMIN))));
                
                repo.deleteAll();
                IntStream.range(0, 1000).parallel().forEach(value -> {
                    try {
                        SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken("admin", "admin",
                                        singleton(new SimpleGrantedAuthority(Authorities.ADMIN))));
                        
                        final Random rnd = new Random();
                        final List<DataPoint> source = new ArrayList<>();
                        
                        IntStream.range(0, 10).forEach(i -> source.add(
                                new DataPoint(
                                        new Quantity(rnd.nextDouble(), rnd.nextDouble(), "MeV"),
                                        new Quantity(rnd.nextDouble(), rnd.nextDouble(), "mb"))));
                        
                        final List<Approximation> approximations = new ArrayList<>();
                        
                        IntStream.range(0, 2).forEach(i -> {
                            final List<Curve> curves = new ArrayList<>();
                            
                            IntStream.range(0, 2).forEach(j -> {
                                final Curve c = new Curve();
                                c.setType(rnd.nextBoolean() ?
                                        FittingService.Curves.GAUSSIAN :
                                        FittingService.Curves.LORENTZIAN);
                                c.setEnergyAtMaxCrossSection(new Quantity(rnd.nextDouble(), rnd.nextDouble(), "MeV"));
                                c.setFullWidthAtHalfMaximum(new Quantity(rnd.nextDouble(), rnd.nextDouble(), "MeV"));
                                c.setMaxCrossSection(new Quantity(rnd.nextDouble(), rnd.nextDouble(), "mb"));
                                
                                curves.add(c);
                            });
                            
                            final double chiSquared = rnd.nextDouble() * 1000;
                            
                            final Approximation a = new Approximation();
                            a.setChiSquared(chiSquared);
                            a.setChiSquaredReduced(chiSquared / (source.size() - (curves.size() * 3)));
                            a.setDescription("Sample data " + rnd.nextInt());
                            a.setSourceData(source);
                            a.setCurves(curves);
                            
                            approximations.add(a);
                        });
                        
                        final Reaction reaction1 = new Reaction();
                        reaction1.setIncident("A");
                        reaction1.setOutgoing("B");
                        reaction1.setTarget(new Nucleus(rnd.nextInt(100) + 1, rnd.nextInt(100) + 1));
                        reaction1.setProduct(new Nucleus(rnd.nextInt(100) + 1, rnd.nextInt(100) + 1));
                        
                        final Reaction reaction2 = new Reaction();
                        reaction2.setIncident("C");
                        reaction2.setOutgoing("D");
                        reaction2.setTarget(new Nucleus(rnd.nextInt(100) + 1, rnd.nextInt(100) + 1));
                        reaction2.setProduct(new Nucleus(rnd.nextInt(100) + 1, rnd.nextInt(100) + 1));
                        
                        final Record record = new Record();
                        record.setId(UUID.randomUUID().toString());
                        record.setEnergyCenter(new Quantity(rnd.nextDouble(), rnd.nextDouble(), "MeV"));
                        record.setFirstMoment(new Quantity(rnd.nextDouble(), rnd.nextDouble(), "mb"));
                        record.setIntegratedCrossSection(new Quantity(rnd.nextDouble(), rnd.nextDouble(), "MeV*mb"));
                        record.setReactions(Arrays.asList(reaction1, reaction2));
                        record.setSourceData(source);
                        record.setApproximations(approximations);
                        record.setExforNumber(UUID.randomUUID().toString()
                                .toUpperCase().replace("-", "").substring(0, 8));
                        
                        repo.insert(record);
                    } finally {
                        SecurityContextHolder.getContext().setAuthentication(null);
                    }
                });
            } finally {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        };
    }
}
