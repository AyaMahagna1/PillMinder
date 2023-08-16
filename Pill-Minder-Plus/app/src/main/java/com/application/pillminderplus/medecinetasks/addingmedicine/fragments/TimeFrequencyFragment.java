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

public class TimeFrequencyFragment extends Fragment implements View.OnClickListener {

    Bundle savedInstanceState;
    public TimeFrequencyFragment() {
    }

    public static TimeFrequencyFragment newInstance() {
        TimeFrequencyFragment fragment = new TimeFrequencyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_frequency, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String toolbarTitle = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().getName();
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText(toolbarTitle);
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("How many times a day do you take this medicine?");
        this.savedInstanceState = savedInstanceState;
        view.findViewById(R.id.text_view_once_daily_add_med_time_frequency).setOnClickListener(this);
        view.findViewById(R.id.text_view_twice_daily_add_med_time_frequency).setOnClickListener(this);
        view.findViewById(R.id.text_view_3_times_a_day_add_med_time_frequency).setOnClickListener(this);
        view.findViewById(R.id.text_view_4_times_a_day_add_med_time_frequency).setOnClickListener(this);
    }
// saving data and go to next fragment
    @Override
    public void onClick(View v) {
        if(((TextView) v).getId() == R.id.text_view_once_daily_add_med_time_frequency) {
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setTimeFrequency(1);
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().setTimeFrequency(1);
        }
        else if(((TextView) v).getId() == R.id.text_view_twice_daily_add_med_time_frequency) {
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setTimeFrequency(2);
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().setTimeFrequency(2);
        }
        else if(((TextView) v).getId() == R.id.text_view_3_times_a_day_add_med_time_frequency) {
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setTimeFrequency(3);
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().setTimeFrequency(3);
        }
        else if(((TextView) v).getId() == R.id.text_view_4_times_a_day_add_med_time_frequency) {
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setTimeFrequency(4);
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().setTimeFrequency(4);
        }
        ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new AddTimesFragment());

    }
}