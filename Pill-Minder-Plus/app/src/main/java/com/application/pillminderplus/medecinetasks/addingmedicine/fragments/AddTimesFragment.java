package com.application.pillminderplus.medecinetasks.addingmedicine.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddMedicineActivity;
import com.application.pillminderplus.medecinetasks.addingmedicine.MedicineDayFrequency;
import com.application.pillminderplus.medecinetasks.addingmedicine.adapters.TimesAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AddTimesFragment extends Fragment  {
// Fragment for choosing medicine frequency -> doses
    private int timeFrequency = 1;
    private ArrayList<Integer> amounts;
    private ArrayList<LocalDateTime> times;
    Button nextButton;

    public AddTimesFragment() {
    }

    public static AddTimesFragment newInstance() {
        AddTimesFragment fragment = new AddTimesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_times, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String toolbarTitle = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().getName();
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText(toolbarTitle);
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("When do you need to take this medicine?");
        nextButton = view.findViewById(R.id.button_submit_edit_med);
        nextButton.setVisibility(View.GONE);
        MedicineDayFrequency dayFrequency = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getDayFrequency();
       // if every day choose how many doses a day
        if(dayFrequency == MedicineDayFrequency.EVERYDAY) {
            timeFrequency = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getTimeFrequency();
        }
        initializeAmount();
        setupRecyclerView(view);
        // If next was clicked go to page to choose the start date for taking medicine
        nextButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                times = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getTimes();
                setUnchangedTimes();
                ((AddMedicineActivity) getActivity()).closeKeyboard(view);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().setAmounts(amounts);
                ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new StartDateFragment());
            }
        });
    }
    // Initializing before inserting data
    private void initializeAmount() {
        amounts = new ArrayList<>(timeFrequency);
        for(int i = 0; i < timeFrequency; i++) {
            amounts.add(null);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUnchangedTimes() {
        for(int i = 0; i < times.size(); i++) {
            if(times.get(i) == null) {
                times.set(i, LocalDateTime.now());
            }
        }
    }
// Adapter
    private void setupRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_med_times_add_med);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        TimesAdapter timesAdapter = new TimesAdapter(getContext(), timeFrequency, this);
        recyclerView.setAdapter(timesAdapter);
    }

// Adding the chosen amount of pills/medicine
    public void putAmount(int index, Integer amount) {
        amounts.set(index, amount);
        if(!isAllAmountsSet()) {
            nextButton.setVisibility(View.GONE);
        }
        else {
            nextButton.setVisibility(View.VISIBLE);
        }
    }
// Adding the chosen time
    public void putTime(int index, LocalDateTime time) {
        ((AddMedicineActivity) getActivity()).getAddMedPresenter().putTime(index, time);
    }
// If all values were inserted
    private boolean isAllAmountsSet() {
        for(int i = 0; i < amounts.size(); i++) {
            if(amounts.get(i) == null) {
                return false;
            }
        }
        return  true;
    }
}