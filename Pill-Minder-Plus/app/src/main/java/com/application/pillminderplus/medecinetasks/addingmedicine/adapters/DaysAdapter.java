package com.application.pillminderplus.medecinetasks.addingmedicine.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.fragments.WeekDaysFragment;
// Adapter for week days
public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.ViewHolder> {
    Context context;
    WeekDaysFragment fragment;

    public DaysAdapter(Context context, WeekDaysFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.week_day_row, parent, false);
        return new ViewHolder(v);
    }

    // update the ViewHolder contents with the item at the given position and also sets up some private fields to be used by RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i = position;
        holder.dayTextView.setText(fragment.getDays().get(i).first.getDay());

        holder.constraintLayout.setOnClickListener(v -> {
            fragment.setDay(i, !fragment.getDays().get(i).second);
            if (fragment.getDays().get(i).second) {
                holder.dayTextView.setBackgroundColor(context.getResources().getColor(R.color.dodger_blue));
                holder.dayTextView.setTextColor(Color.WHITE);
            } else {
                holder.dayTextView.setBackgroundColor(Color.WHITE);
                holder.dayTextView.setTextColor(context.getResources().getColor(R.color.grey));
            }
        });
    }

    // Returns the total number of items in the data set held by the adapter.
    @Override
    public int getItemCount() {
        return 7;
    }

    // inner class
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dayTextView;
        ConstraintLayout constraintLayout;
        View layout;

        public ViewHolder(@NonNull View view) {
            super(view);
            layout = view;
            constraintLayout = view.findViewById(R.id.constraint_layout_week_day_row_add_med);
            dayTextView = view.findViewById(R.id.text_view_week_day_add_med);
        }

        public ConstraintLayout getConstraintLayout() {
            return constraintLayout;
        }

        public void setConstraintLayout(ConstraintLayout constraintLayout) {
            this.constraintLayout = constraintLayout;
        }

        public View getLayout() {
            return layout;
        }

        public void setLayout(View layout) {
            this.layout = layout;
        }
    }
}
