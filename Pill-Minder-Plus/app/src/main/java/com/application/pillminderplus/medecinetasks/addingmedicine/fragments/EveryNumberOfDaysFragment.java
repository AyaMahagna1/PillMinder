package com.application.pillminderplus.medecinetasks.addingmedicine.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddMedicineActivity;

public class EveryNumberOfDaysFragment extends Fragment {

    public EveryNumberOfDaysFragment() {
    }
    public static EveryNumberOfDaysFragment newInstance() {
        EveryNumberOfDaysFragment fragment = new EveryNumberOfDaysFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_every_number_of_days, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String toolbarTitle = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().getName();
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText(toolbarTitle);
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("How many days between 2 doses?");
        Button nextButton = ((Button) view.findViewById(R.id.button_submit_edit_med));
        nextButton.setVisibility(View.GONE);
        EditText numEditText = ((EditText) view.findViewById(R.id.edit_text_every_number_of_days_add_med));
        numEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // do nothing
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length() == 0) {
                    //There is no text -> can't proceed to next fragment
                    nextButton.setVisibility(View.GONE);
                }
                else {
                    //Show next button
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });
        // Save/send data and go to next fragment
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddMedicineActivity) getActivity()).closeKeyboard(view);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().setDaysBetweenDoses(Integer.parseInt(numEditText.getText().toString()));
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setEveryNDays(Integer.parseInt(numEditText.getText().toString()));
                ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new AddTimesFragment());
            }
        });
    }
}