package com.example.wgumobilelegit.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Objects.Course;
import com.example.wgumobilelegit.Objects.Term;
import com.example.wgumobilelegit.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private List<Course> courseList;

    private int selectedItem = -1;
    private Course selectedCourse;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView StartDate;
        public TextView EndDate;
        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.courseItemTitle);
            StartDate = v.findViewById(R.id.courseItemStart);
            EndDate = v.findViewById(R.id.courseItemEnd);
        }
    }

    // Callback interface
    public interface OnCourseSelectedListener {
        void onCourseSelected(Course selectedCourse);
    }

    // Instance of the callback interface
    private OnCourseSelectedListener courseSelectedListener;

    // Constructor
    public CourseAdapter(List<Course> courseList, OnCourseSelectedListener courseSelectedListener) {
        this.courseList = courseList;
        this.courseSelectedListener = courseSelectedListener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (holder.getAdapterPosition() == selectedItem) {
            // Change background color for selected item
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            // Change background color for other items
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the selected item when an item is clicked
                selectedItem = position;
                notifyDataSetChanged();

                selectedCourse = courseList.get(position);
                courseSelectedListener.onCourseSelected(selectedCourse);
            }
        });

        // Check if the courseList is not null and has data
        if(courseList != null && !courseList.isEmpty()) {
            // Get element from your data set at this position and replace the contents of the view
            Course course = courseList.get(position);
            if (course != null && course.getCourseName() != null) {
                holder.title.setText(course.getCourseName());
                holder.StartDate.setText(course.getStartDate().toString());
                holder.EndDate.setText(course.getEndDate().toString());
            } else {
                // handle case where course or courseName is null
            }
        } else {
            // handle case where courseList is null or empty
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        // Check if the courseList is not null before returning its size
        return courseList != null ? courseList.size() : 0;
    }

}
