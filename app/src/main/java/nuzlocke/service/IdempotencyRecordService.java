package nuzlocke.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import nuzlocke.domain.IdempotencyRecord;
import nuzlocke.repository.IdempotencyRecordRepository;

@Service
public class IdempotencyRecordService {

    private final IdempotencyRecordRepository idempotencyRepo;

    @Autowired
    public IdempotencyRecordService(IdempotencyRecordRepository idempotencyRepo) {
        this.idempotencyRepo = idempotencyRepo;
    }

    @Transactional
    public IdempotencyRecord fetchOrReserve(String key) {
        if (key == null || key.isBlank())
            return null;

        Optional<IdempotencyRecord> existingRecord = idempotencyRepo.findById(key);
        if (existingRecord.isPresent())
            return existingRecord.get();

        try {
            IdempotencyRecord placeholder = new IdempotencyRecord(key, null, 0);
            idempotencyRepo.save(placeholder);
            return null;
        } catch (DataIntegrityViolationException exception) {
            return idempotencyRepo.findById(key).orElse(null);
        }
    }

    @Transactional
    public void saveRecord(String key, String responseBody, int status) {
        IdempotencyRecord record = idempotencyRepo.findById(key).orElseThrow();
        record.setResponse(responseBody);
        record.setStatusCode(status);
        idempotencyRepo.save(record);
    }
}
