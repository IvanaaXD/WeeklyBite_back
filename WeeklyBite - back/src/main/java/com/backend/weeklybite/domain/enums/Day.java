package com.backend.weeklybite.domain.enums;

public enum Day {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private final int value;

    Day(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Day fromValue(int value) {
        for (Day day : Day.values()) {
            if (day.value == value) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid day number: " + value);
    }
}
