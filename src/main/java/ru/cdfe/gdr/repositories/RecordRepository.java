package ru.cdfe.gdr.repositories;

import ru.cdfe.gdr.domain.Record;

public interface RecordRepository extends SecuredMongoRepository<Record, String> {}
