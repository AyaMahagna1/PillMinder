package com.application.pillminderplus.medecinetasks.addingmedicine.adapters;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.fragments.AddTimesFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
//Medicine times adapter
public class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.ViewHolder> {
    Context context;
    int count;
    AddTimesFragment fragment;

    public TimesAdapter(Context context, int count, AddTimesFragment fragment) {
        this.context = context;
        this.count = count;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.med_time_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int i = position;
        // Adapt it to dosses number
        switch (i) {
            case 0:
                holder.doseNumberTextView.setText("First dose");
                break;
            case 1:
                holder.doseNumberTextView.setText("Second dose");
                break;
            case 2:
                holder.doseNumberTextView.setText("Third dose");
                break;
            case 3:
                holder.doseNumberTextView.setText("Fourth dose");
                break;
        }
        holder.unitNameTextView.setText("Pills");

        holder.numberOfUnitsEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    fragment.putAmount(i, null);
                } else {
                    fragment.putAmount(i, Integer.parseInt(s.toString()));
                }
            }
        });

        holder.timePicker.setIs24HourView(true);
        holder.timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                fragment.putTime(i, LocalDateTime.of(LocalDate.now(), LocalTime.of(holder.timePicker.getHour(), holder.timePicker.getMinute(), 0)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return count;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView doseNumberTextView;
        TextView unitNameTextView;
        EditText numberOfUnitsEditText;
        TimePicker timePicker;
        ConstraintLayout constraintLayout;
        View layout;

        public ViewHolder(@NonNull View view) {
            super(view);
            layout = view;
            constraintLayout = view.findViewById(R.id.constraint_layout_med_time_row);
            timePicker = view.findViewById(R.id.time_picker_med_time_row_add_med);
            doseNumberTextView = view.findViewById(R.id.text_view_dose_number_med_time_row);
            unitNameTextView = view.findViewById(R.id.text_view_unit_med_time_row_add_med);
            numberOfUnitsEditText = view.findViewById(R.id.edit_text_number_med_time_row_add_med);
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
