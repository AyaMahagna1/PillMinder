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

// First screen for adding a medicine name
public class AddNameFragment extends Fragment {


    public AddNameFragment() {
    }

    public static AddNameFragment newInstance() {
        AddNameFragment fragment = new AddNameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_name, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText("");
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("What is the name of the medicine that you would like to add?");
        Button nextButton = view.findViewById(R.id.button_submit_edit_med);
        nextButton.setVisibility(View.GONE);
        EditText nameEditText = view.findViewById(R.id.edit_text_med_name_add_med);
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 //do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    //If there was no text you can't proceed to next page
                    nextButton.setVisibility(View.GONE);
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });

        view.findViewById(R.id.button_submit_edit_med).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to next page -> choose the form of the medicine
                ((AddMedicineActivity) getActivity()).closeKeyboard(view);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setName(nameEditText.getText().toString());
                ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new FormFragment());
            }
        });
    }
}