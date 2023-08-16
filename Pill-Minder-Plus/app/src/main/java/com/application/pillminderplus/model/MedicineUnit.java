package com.application.pillminderplus.model;
//We saw that there is no need for units and strength of medicine so we decided to delete the rest
public enum MedicineUnit {
    g("g");
    private String unit;

    MedicineUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
