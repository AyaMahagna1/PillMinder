package com.application.pillminderplus.medecinetasks.addingmedicine.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddMedicineActivity;


public class AddReasonFragment extends Fragment {
    // Adding reason for taking this med fragment
    public AddReasonFragment() {
    }

    public static AddReasonFragment newInstance() {
        AddReasonFragment fragment = new AddReasonFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_reason, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String toolbarTitle = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().getName();
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText(toolbarTitle);
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("Why are you taking this medicine?");
        EditText reasonEditText = view.findViewById(R.id.edit_text_med_reason_add_med);
        Button nextButton = view.findViewById(R.id.button_submit_edit_med);
        nextButton.setVisibility(View.GONE);
        reasonEditText.addTextChangedListener(new TextWatcher() {
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
                    //If there was no reason submitted you can't proceed to next page
                    nextButton.setVisibility(View.GONE);
                }
                else{
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });
        view.findViewById(R.id.button_submit_edit_med).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save the reason and proceed for next fragment/page -> med frequency
                ((AddMedicineActivity) getActivity()).closeKeyboard(view);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setReason(reasonEditText.getText().toString());
                ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new DayFrequenceFragment());
            }
        });
    }
}