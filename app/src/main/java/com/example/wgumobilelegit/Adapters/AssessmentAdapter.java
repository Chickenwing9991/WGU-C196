package com.example.wgumobilelegit.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Objects.Assessment;
import com.example.wgumobilelegit.R;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.ViewHolder> {
    private List<Assessment> assessmentList;

    private int selectedItem = -1;
    private Assessment selectedAssessment;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView DueDate;
        public TextView StartDate;
        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.assessmentItemTitle);
            DueDate = v.findViewById(R.id.assessmentItemDue);
            StartDate = v.findViewById(R.id.assessmentItemStart);
        }
    }

    // Callback interface
    public interface OnAssessmentSelectedListener {
        void onAssessmentSelected(Assessment selectedAssessment);
    }

    // Instance of the callback interface
    private OnAssessmentSelectedListener assessmentSelectedListener;

    // Constructor
    public AssessmentAdapter(List<Assessment> assessmentList, OnAssessmentSelectedListener assessmentSelectedListener) {
        this.assessmentList = assessmentList;
        this.assessmentSelectedListener = assessmentSelectedListener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public AssessmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.assessment_item, parent, false);
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

                selectedAssessment = assessmentList.get(position);
                assessmentSelectedListener.onAssessmentSelected(selectedAssessment);
            }
        });

        // Check if the assessmentList is not null and has data
        if(assessmentList != null && !assessmentList.isEmpty()) {
            // Get element from your data set at this position and replace the contents of the view
            Assessment assessment = assessmentList.get(position);
            if (assessment != null && assessment.getAssessmentName() != null) {
                holder.title.setText(assessment.getAssessmentName());
                holder.DueDate.setText(assessment.getDueDate().toString());
                holder.StartDate.setText(assessment.getStartDate().toString());
            } else {
                // handle case where assessment or assessmentName is null
            }
        } else {
            // handle case where assessmentList is null or empty
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        // Check if the assessmentList is not null before returning its size
        return assessmentList != null ? assessmentList.size() : 0;
    }

}
