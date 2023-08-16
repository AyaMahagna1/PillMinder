package com.application.pillminderplus.medecinetasks.addingmedicine.fragments;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddMedicineActivity;
import com.application.pillminderplus.medecinetasks.addingmedicine.WeekDays;
import com.application.pillminderplus.medecinetasks.addingmedicine.adapters.DaysAdapter;

import java.util.ArrayList;
import java.util.Objects;

//If the user take the medicine in specific day
public class WeekDaysFragment extends Fragment  {
    // every day in the week included or no
    ArrayList<Pair<WeekDays, Boolean>> days;
    Button nextButton;
    public WeekDaysFragment() {
    }
    public static WeekDaysFragment newInstance() {
        WeekDaysFragment fragment = new WeekDaysFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_week_days, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String toolbarTitle = ((AddMedicineActivity) Objects.requireNonNull(getActivity())).getAddMedPresenter().getMedicine().getName();
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText(toolbarTitle);
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("Choose days to take the medicine in it");
        nextButton = view.findViewById(R.id.button_submit_edit_med);
        nextButton.setVisibility(View.GONE);
        initializeDays();
        setupRecyclerView(view);
        // Save and go to next page
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<WeekDays> selectedDays = new ArrayList<>();
                StringBuilder selectedWeekDaysStr = new StringBuilder("");
                for(int i = 0; i < days.size(); i++) {
                    if(days.get(i).second) {
                        selectedDays.add(days.get(i).first);
                        selectedWeekDaysStr.append(days.get(i).first.getDay() + ", ");
                    }
                }
                selectedWeekDaysStr.delete(0, selectedWeekDaysStr.length() - 2);
                ((AddMedicineActivity) Objects.requireNonNull(getActivity())).closeKeyboard(view);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().setDays(selectedDays);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setWeekDays(selectedWeekDaysStr.toString());
                ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new AddTimesFragment());
            }
        });
    }
    private void initializeDays() {
        days = new ArrayList<>();
        days.add(new Pair<>(WeekDays.SATURDAY, false));
        days.add(new Pair<>(WeekDays.SUNDAY, false));
        days.add(new Pair<>(WeekDays.MONDAY, false));
        days.add(new Pair<>(WeekDays.TUESDAY, false));
        days.add(new Pair<>(WeekDays.WEDNESDAY, false));
        days.add(new Pair<>(WeekDays.THURSDAY, false));
        days.add(new Pair<>(WeekDays.FRIDAY, false));
    }
    //Adapter
    private void setupRecyclerView(@NonNull View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_week_days_add_med);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DaysAdapter adapter = new DaysAdapter(getContext(), this);
        recyclerView.setAdapter(adapter);
    }
    // If day was selected remove it from the list
    public void setDay(int index, boolean isSelected) {
        days.set(index, new Pair(days.get(index).first, isSelected));
        if(isDaySelected()) {
            nextButton.setVisibility(View.VISIBLE);
        }
        else {
            nextButton.setVisibility(View.GONE);
        }
    }
    // Load according selected or not
    private boolean isDaySelected() {
        for(int i = 0; i < days.size(); i++) {
            if(days.get(i).second) {
                return true;
            }
        }
        return false;
    }

   public ArrayList<Pair<WeekDays, Boolean>> getDays() {
        return days;
    }

}