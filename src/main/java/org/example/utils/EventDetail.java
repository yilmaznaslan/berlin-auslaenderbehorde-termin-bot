package org.example.utils;

import java.util.Objects;

public class EventDetail {

    private final String operation;
    private final String status;

    public EventDetail(String operation, String status) {
        this.operation = operation;
        this.status = status;
    }

    public String getOperation() {
        return operation;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDetail that = (EventDetail) o;
        return Objects.equals(operation, that.operation) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operation, status);
    }
}
