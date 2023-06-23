package com.example.wgumobilelegit.Objects;

import com.example.wgumobilelegit.Objects.CourseStatus;

public class SpinnerItem {
    private String displayName;
    private CourseStatus status;

    public SpinnerItem(String displayName, CourseStatus status) {
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
