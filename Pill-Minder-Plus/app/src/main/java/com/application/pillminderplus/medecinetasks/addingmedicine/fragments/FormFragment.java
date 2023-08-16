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
import com.application.pillminderplus.medecinetasks.addingmedicine.MedicineForm;
import com.application.pillminderplus.model.MedicineUnit;

// Choosing the form of the medicine
public class FormFragment extends Fragment implements View.OnClickListener {
    Bundle savedInstanceState;
    String medForm;

    public FormFragment() {
    }

    public static FormFragment newInstance() {
        FormFragment fragment = new FormFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String toolbarTitle = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().getName();
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText(toolbarTitle);
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("What is the form of the medicine?");
        this.savedInstanceState = savedInstanceState;
        view.findViewById(R.id.text_view_pills_add_med_form).setOnClickListener(this);
        view.findViewById(R.id.text_view_solution_add_med_form).setOnClickListener(this);
        view.findViewById(R.id.text_view_injection_add_med_form).setOnClickListener(this);
        view.findViewById(R.id.text_view_powder_add_med_form).setOnClickListener(this);
        view.findViewById(R.id.text_view_drops_add_med_form).setOnClickListener(this);
        view.findViewById(R.id.text_view_inhaler_add_med_form).setOnClickListener(this);
        view.findViewById(R.id.text_view_topical_add_med_form).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (((TextView) v).getId() == R.id.text_view_pills_add_med_form) {
            medForm = MedicineForm.PILLS.getForm();
        } else if (((TextView) v).getId() == R.id.text_view_solution_add_med_form) {
            medForm = MedicineForm.SOLUTION.getForm();
        } else if (((TextView) v).getId() == R.id.text_view_injection_add_med_form) {
            medForm = MedicineForm.INJECTION.getForm();
        } else if (((TextView) v).getId() == R.id.text_view_powder_add_med_form) {
            medForm = MedicineForm.POWDER.getForm();
        } else if (((TextView) v).getId() == R.id.text_view_drops_add_med_form) {
            medForm = MedicineForm.DROPS.getForm();
        } else if (((TextView) v).getId() == R.id.text_view_inhaler_add_med_form) {
            medForm = MedicineForm.INHALER.getForm();
        } else if (((TextView) v).getId() == R.id.text_view_topical_add_med_form) {
            medForm = MedicineForm.TOPICAL.getForm();
        }
        ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setForm(medForm);
        ((AddMedicineActivity)getActivity()).getAddMedPresenter().getMedicine().setUnit(MedicineUnit.g.getUnit());
        ((AddMedicineActivity)getActivity()).getAddMedPresenter().getMedicine().setStrength(50);
        ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState,new AddReasonFragment());
    }
}