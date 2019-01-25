package com.furkanturkmen.gradeslist.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.furkanturkmen.gradeslist.R;
import com.furkanturkmen.gradeslist.models.Grade;

import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder> {
    private List<Grade> mGrades;
    final private GradeClickListener mGradeClickListener;

    public interface GradeClickListener{
        void gradeOnClick (int i);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView dateTextView;
        private TextView nameTextView;
        private TextView scoreTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.textViewDate);
            nameTextView = itemView.findViewById(R.id.textViewName);
            scoreTextView = itemView.findViewById(R.id.textViewScore);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mGradeClickListener.gradeOnClick(clickedPosition);
        }
    }

    public GradeAdapter(List<Grade> grades, GradeClickListener mGradeClickListener) {
        this.mGrades = grades;
        this.mGradeClickListener = mGradeClickListener;
    }

    @Override
    public GradeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.grid_cell, null);

        // Return a new holder instance

        GradeAdapter.ViewHolder viewHolder = new GradeAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GradeAdapter.ViewHolder holder, int position) {

        Grade grade = mGrades.get(position);

        holder.nameTextView.setText(grade.getGradeText());
        holder.scoreTextView.setText(grade.getGradeScore());
        holder.dateTextView.setText(grade.getGradeDate() );

        double scoreGoodOrBad;
        for (int i = 0; i < mGrades.size(); i++) {
            scoreGoodOrBad = Double.parseDouble(holder.scoreTextView.getText().toString());
            if(scoreGoodOrBad >= 5.5){
                holder.scoreTextView.setTextColor(Color.GREEN);
            } else{
                holder.scoreTextView.setTextColor(Color.RED);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mGrades.size();
    }

    public void swapList (List<Grade> newList) {
        mGrades = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
}
