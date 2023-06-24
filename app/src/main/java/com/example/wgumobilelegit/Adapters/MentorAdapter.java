package com.example.wgumobilelegit.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Objects.Mentor;
import com.example.wgumobilelegit.R;

import java.util.List;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.ViewHolder> {
    private List<Mentor> mentorList;

    private int selectedItem = -1;
    private Mentor selectedMentor;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView email;
        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.mentorItemTitle);
            email = v.findViewById(R.id.mentorItemEmail);
        }
    }

    // Callback interface
    public interface OnMentorSelectedListener {
        void onMentorSelected(Mentor selectedMentor);
    }

    // Instance of the callback interface
    private OnMentorSelectedListener mentorSelectedListener;

    // Constructor
    public MentorAdapter(List<Mentor> mentorList, OnMentorSelectedListener mentorSelectedListener) {
        this.mentorList = mentorList;
        this.mentorSelectedListener = mentorSelectedListener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public MentorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mentor_item, parent, false);
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

                selectedMentor = mentorList.get(position);
                mentorSelectedListener.onMentorSelected(selectedMentor);
            }
        });

        // Check if the mentorList is not null and has data
        if(mentorList != null && !mentorList.isEmpty()) {
            // Get element from your data set at this position and replace the contents of the view
            Mentor mentor = mentorList.get(position);
            if (mentor != null && mentor.getName() != null) {
                holder.title.setText(mentor.getName());
                holder.email.setText(mentor.getEmail());
            } else {
                // handle case where mentor or mentorName is null
            }
        } else {
            // handle case where mentorList is null or empty
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        // Check if the mentorList is not null before returning its size
        return mentorList != null ? mentorList.size() : 0;
    }

}
