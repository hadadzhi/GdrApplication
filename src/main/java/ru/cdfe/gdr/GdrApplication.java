package ru.cdfe.gdr;

import com.mongodb.MongoClientOptions;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import ru.cdfe.gdr.domain.Record;
import ru.cdfe.gdr.service.SearchService;
import ru.cdfe.gdr.service.mongo.MongoTemplateSearchService;

import javax.validation.Validator;

@Slf4j
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        WebMvcAutoConfiguration.class,
})
@EnableMongoAuditing
@EnableMongoRepositories
public class GdrApplication {
    public static void main(String[] args) {
        SpringApplication.run(GdrApplication.class, args);
    }
    
    @Bean
    public CurieProvider curieProvider(GdrProperties conf) {
        return new DefaultCurieProvider(conf.getCurieName(), new UriTemplate(conf.getCurieUrlTemplate()));
    }
    
    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }
    
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
    
    @Bean
    public MongoClientOptions mongoClientOptions() {
        return MongoClientOptions.builder()
                .writeConcern(WriteConcern.MAJORITY)
                .readConcern(ReadConcern.MAJORITY)
                .build();
    }
    
    @Bean
    public SearchService<Record> recordSearchService(MongoTemplate mongo) {
        return new MongoTemplateSearchService<>(Record.class, mongo);
    }
}
