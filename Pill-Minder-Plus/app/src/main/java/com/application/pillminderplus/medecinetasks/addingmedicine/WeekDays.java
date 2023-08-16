package com.application.pillminderplus.medecinetasks.addingmedicine;

public enum WeekDays {

    SUNDAY("sunday"),
    MONDAY("monday"),
    TUESDAY("tuesday"),
    WEDNESDAY("wednesday"),
    THURSDAY("thursday"),
    FRIDAY("friday"),
    SATURDAY("saturday");

    private String day;

    WeekDays(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }
}
