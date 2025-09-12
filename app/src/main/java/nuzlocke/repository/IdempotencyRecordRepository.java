package nuzlocke.repository;

import org.springframework.data.repository.CrudRepository;

import nuzlocke.domain.IdempotencyRecord;

public interface IdempotencyRecordRepository extends CrudRepository<IdempotencyRecord, String> {
}
