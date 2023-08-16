package com.application.pillminderplus.medecinetasks.addingmedicine.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddMedicineActivity;

import java.time.LocalDate;

// The end date for taking this medicine
public class EndDateFragment extends Fragment {
    public EndDateFragment() {
    }
    public static EndDateFragment newInstance() {
        EndDateFragment fragment = new EndDateFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_end_date, container, false);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String toolbarTitle = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().getName();
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText(toolbarTitle);
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("When will you stop taking this medicine?");
        DatePicker endDatePicker = view.findViewById(R.id.date_picker_end_date_add_med);
        LocalDate startDate = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getStartDate();
        startDate = startDate.plusDays(1);
        endDatePicker.setMinDate(startDate.toEpochDay()*24*60*60*1000);
        LocalDate maxDate = startDate.plusYears(1);
        endDatePicker.setMaxDate(maxDate.toEpochDay()*24*60*60*1000);
        // Choosing the end date
        view.findViewById(R.id.button_submit_edit_med).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                LocalDate pickerEndDate = LocalDate.of(endDatePicker.getYear(), endDatePicker.getMonth() + 1, endDatePicker.getDayOfMonth());
                String endDate = pickerEndDate.toString();
                ((AddMedicineActivity) getActivity()).closeKeyboard(view);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().setEndDate(pickerEndDate);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setEndDate(endDate);
                ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new RefillReminderFragment());
            }
        });
    }
}