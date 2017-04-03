package ru.cdfe.gdr.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.cdfe.gdr.domain.Record;

public interface RecordRepository extends MongoRepository<Record, String> {
    Page<Record> findByExforNumber(String subent, Pageable pageable);
}
