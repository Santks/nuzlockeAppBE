package nuzlocke.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class IdempotencyRecord {

    @Id
    private String idempotencyKey;

    private String response;

    private int statusCode;

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    protected IdempotencyRecord() {

    }

    public IdempotencyRecord(String key, String resp, int status) {
        this.idempotencyKey = key;
        this.response = resp;
        this.statusCode = status;
    }

    @Override
    public String toString() {
        return "IdempotencyRecord [idempotencyKey=" + idempotencyKey + ", response=" + response + ", statusCode="
                + statusCode + "]";
    }

}
