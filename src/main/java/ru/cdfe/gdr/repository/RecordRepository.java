package ru.cdfe.gdr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.cdfe.gdr.domain.Record;

public interface RecordRepository extends MongoRepository<Record, String> {
    @Query("{'exforNumber' : {'$regex' : ?0 }}")
    Page<Record> findBySubentRegex(String subentRegex, Pageable pageable);
}
