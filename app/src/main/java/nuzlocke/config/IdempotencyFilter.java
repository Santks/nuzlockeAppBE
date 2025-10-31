package nuzlocke.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nuzlocke.domain.IdempotencyRecord;
import nuzlocke.repository.IdempotencyRecordRepository;

@Component
public class IdempotencyFilter implements HandlerInterceptor {

    private final IdempotencyRecordRepository idempotencyRepo;

    @Autowired
    public IdempotencyFilter(IdempotencyRecordRepository idempotencyRepo) {
        this.idempotencyRepo = idempotencyRepo;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws IOException {
        String key = req.getHeader("Idempotency-Key");
        if (key != null) {
            var record = idempotencyRepo.findById(key);
            if (record.isPresent()) {
                IdempotencyRecord foundRecord = record.get();
                res.setStatus(foundRecord.getStatusCode());
                res.getWriter().write(foundRecord.getResponse());
                return false;
            }
        }
        return true;
    }

}
