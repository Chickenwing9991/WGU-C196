package com.example.wgumobilelegit.Objects;

public enum CourseStatus {
    InProgress,
    Completed,
    Dropped,
    PlanToTake;

    public static CourseStatus fromString(String name) {
        if (name == null) return null; // or some default value
        try {
            return CourseStatus.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null; // or some default value
        }
    }
}



