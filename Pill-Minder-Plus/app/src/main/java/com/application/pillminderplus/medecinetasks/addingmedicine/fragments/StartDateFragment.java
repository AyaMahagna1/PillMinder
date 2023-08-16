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

// Choosing a date to start taking the medicine
public class StartDateFragment extends Fragment {
    public StartDateFragment() {
    }
    public static StartDateFragment newInstance() {
        StartDateFragment fragment = new StartDateFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start_date, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        String toolbarTitle = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().getName();
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText(toolbarTitle);
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("When will you start taking this medicine?");
        DatePicker startDatePicker = view.findViewById(R.id.date_picker_start_date_add_med);
        startDatePicker.setMinDate(System.currentTimeMillis());
        // The user chose a date -> Save it and go to next fragment
        view.findViewById(R.id.button_submit_edit_med).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalDate pickerStartDate = LocalDate.of(startDatePicker.getYear(), startDatePicker.getMonth() + 1 , startDatePicker.getDayOfMonth());
                String startDate = pickerStartDate.toString();
                ((AddMedicineActivity) getActivity()).closeKeyboard(view);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().setStartDate(pickerStartDate);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setStartDate(startDate);
                ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new EndDateFragment());
            }
        });

    }
}