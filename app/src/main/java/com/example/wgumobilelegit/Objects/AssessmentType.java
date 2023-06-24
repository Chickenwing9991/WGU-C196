package com.example.wgumobilelegit.Objects;

// Enum class for AssessmentType
public enum AssessmentType {
    Performance,
    Objective;

    // Method to convert string to AssessmentType
    public static AssessmentType fromString(String name) {
        // If name is null, return null
        if (name == null) return null;
        try {
            // Try to convert the string to AssessmentType
            return AssessmentType.valueOf(name);
        } catch (IllegalArgumentException e) {
            // If conversion fails, return null
            return null;
        }
    }
}
