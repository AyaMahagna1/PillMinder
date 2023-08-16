package com.application.pillminderplus.medecinetasks.addingmedicine;

public enum MedicineDayFrequency {

    EVERYDAY("Everyday"),
    SPECIFIC_DAYS("Specific days"),
    EVERY_NUMBER_OF_DAYS("Every number of days");


    private String frequency;

    MedicineDayFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequency() {
        return frequency;
    }
}
