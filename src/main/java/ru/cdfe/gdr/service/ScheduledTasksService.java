package ru.cdfe.gdr.service;

import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.cdfe.gdr.security.GdrAuthenticationToken;

import java.time.Instant;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Service
public class ScheduledTasksService {
    private final MongoTemplate mongo;
    
    @Autowired
    public ScheduledTasksService(MongoTemplate mongo) {
        this.mongo = mongo;
    }
    
    @Scheduled(
            initialDelay = 0, // Run at startup
            fixedRate = 1000 * 60 * 60 // Run once an hour after startup
    )
    public void purgeExpiredAuthenticationTokens() {
        log.debug("Purging expired authentication tokens");
        final DeleteResult result = mongo.remove(query(where("expiring").lt(Instant.now())),
                GdrAuthenticationToken.class);
        log.debug("Purged expired authentication tokens: {}", result);
    }
}
