package com.example.wgumobilelegit.Objects;

// Class to handle Assessment Type Spinner Items
public class AssessmentTypeSpinnerItem {
    private String displayName; // Display name of the spinner item
    private AssessmentType Type; // Assessment type of the spinner item

    // Constructor for AssessmentTypeSpinnerItem
    public AssessmentTypeSpinnerItem(String displayName, AssessmentType type) {
        this.displayName = displayName;
        this.Type = type;
    }

    // Method to get the display name
    public String getDisplayName() {
        return displayName;
    }

    // Method to get the assessment type
    public AssessmentType getType() {
        return Type;
    }

    // Overriding toString method to return display name
    @Override
    public String toString() {
        return displayName;
    }
}
