package com.application.pillminderplus.medecinetasks.addingmedicine.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddMedicineActivity;
import com.application.pillminderplus.medecinetasks.addingmedicine.MedicineDayFrequency;

public class DayFrequenceFragment extends Fragment implements View.OnClickListener {
    Bundle savedInstanceState;
    public DayFrequenceFragment() {
    }
    public static DayFrequenceFragment newInstance() {
        DayFrequenceFragment fragment = new DayFrequenceFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_frequence, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String toolbarTitle = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().getName();
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText(toolbarTitle);
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("How many times do you take this med?");
        this.savedInstanceState = savedInstanceState;
        view.findViewById(R.id.text_view_everyday_add_med).setOnClickListener(this);
        view.findViewById(R.id.text_view_specific_days_of_the_week_add_med).setOnClickListener(this);
        view.findViewById(R.id.text_view_every_number_of_days_add_med).setOnClickListener(this);
    }
// Check which choice has the user choosed and open the next fragment according to this choice and sending data to next fragment
    @Override
    public void onClick(View v) {
        if(((TextView) v).getId() == R.id.text_view_everyday_add_med) {
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().setDayFrequency(MedicineDayFrequency.EVERYDAY);
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setDayFrequency(MedicineDayFrequency.EVERYDAY.getFrequency());
            ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new TimeFrequencyFragment());
        }
        else if(((TextView) v).getId() == R.id.text_view_specific_days_of_the_week_add_med) {
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().setTimeFrequency(1);
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setTimeFrequency(1);
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setDayFrequency(MedicineDayFrequency.SPECIFIC_DAYS.getFrequency());
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().setDayFrequency(MedicineDayFrequency.SPECIFIC_DAYS);
            ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new WeekDaysFragment());
        }
        else if(((TextView) v).getId() == R.id.text_view_every_number_of_days_add_med) {
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().setTimeFrequency(1);
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setTimeFrequency(1);
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setDayFrequency(MedicineDayFrequency.EVERY_NUMBER_OF_DAYS.getFrequency());
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().setDayFrequency(MedicineDayFrequency.EVERY_NUMBER_OF_DAYS);
            ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new EveryNumberOfDaysFragment());
        }
    }
}