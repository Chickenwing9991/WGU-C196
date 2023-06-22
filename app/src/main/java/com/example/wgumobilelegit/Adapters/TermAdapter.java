package com.example.wgumobilelegit.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wgumobilelegit.Objects.Term;
import com.example.wgumobilelegit.R;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.ViewHolder> {
    private List<Term> termList;
    private int selectedItem = -1;

    private Term selectedTerm;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.termTitle);
        }
    }

    // Callback interface
    public interface OnTermSelectedListener {
        void onTermSelected(Term selectedTerm);
    }

    // Instance of the callback interface
    private OnTermSelectedListener termSelectedListener;

    // Constructor
    public TermAdapter(List<Term> termList, OnTermSelectedListener termSelectedListener) {
        this.termList = termList;
        this.termSelectedListener = termSelectedListener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public TermAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.term_item, parent, false);
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

                selectedTerm = termList.get(position);
                termSelectedListener.onTermSelected(selectedTerm);
            }
        });

        // Check if the termList is not null and has data
        if(termList != null && !termList.isEmpty()) {
            // Get element from your data set at this position and replace the contents of the view
            Term term = termList.get(position);
            if (term != null && term.getTermName() != null) {
                holder.title.setText(term.getTermName());
            } else {
                // handle case where term or termName is null
            }
        } else {
            // handle case where termList is null or empty
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        // Check if the termList is not null before returning its size
        return termList != null ? termList.size() : 0;
    }

}
