package com.application.pillminderplus.model;

public enum DoseStatus {
    TAKEN("taken"),
    SKIPPED("skipped"),
    FUTURE("future"),
    UNKNOWN("unknown");

    private String status;

    DoseStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
