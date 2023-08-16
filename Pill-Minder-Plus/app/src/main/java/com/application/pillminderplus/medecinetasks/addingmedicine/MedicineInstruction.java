package com.application.pillminderplus.medecinetasks.addingmedicine;

public enum MedicineInstruction {

    BEFORE_EATING("Before eating"),
    WHILE_EATING("While eating"),
    AFTER_EATING("After eating");

    private String instruction;

    MedicineInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }
}
