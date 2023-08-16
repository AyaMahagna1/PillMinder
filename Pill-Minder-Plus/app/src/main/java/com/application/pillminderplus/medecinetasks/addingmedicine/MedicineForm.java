package com.application.pillminderplus.medecinetasks.addingmedicine;

public enum MedicineForm {
    PILLS("pills"),
    DROPS("drops"),
    INJECTION("injection"),
    INHALER("inhaler"),
    POWDER("powder"),
    SOLUTION("solution"),
    TOPICAL("topical");

    private String form;

    MedicineForm(String form) {
        this.form = form;
    }

    public String getForm() {
        return form;
    }
}
