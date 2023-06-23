package com.example.wgumobilelegit.Objects;

public class AssessmentTypeSpinnerItem {
    private String displayName;
    private AssessmentType Type;

    public AssessmentTypeSpinnerItem(String displayName, AssessmentType type) {
        this.displayName = displayName;
        this.Type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public AssessmentType getType() {
        return Type;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
