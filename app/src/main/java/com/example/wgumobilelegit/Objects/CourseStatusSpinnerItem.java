package com.example.wgumobilelegit.Objects;

public class CourseStatusSpinnerItem {
    private String displayName;
    private CourseStatus status;

    public CourseStatusSpinnerItem(String displayName, CourseStatus status) {
        this.displayName = displayName;
        this.status = status;
    }

    public String getDisplayName() {
        return displayName;
    }

    public CourseStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
