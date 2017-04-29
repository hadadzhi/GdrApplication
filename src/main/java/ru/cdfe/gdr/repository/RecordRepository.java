package ru.cdfe.gdr.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.cdfe.gdr.domain.Record;

public interface RecordRepository extends MongoRepository<Record, String> {}
