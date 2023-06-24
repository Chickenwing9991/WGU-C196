package com.example.wgumobilelegit.Objects;

public enum AssessmentType {
    Performance,

    Objective;

    public static AssessmentType fromString(String name) {
        if (name == null) return null; // or some default value
        try {
            return AssessmentType.valueOf(name);
        } catch (IllegalArgumentException e) {
            return null; // or some default value
        }
    }
}
