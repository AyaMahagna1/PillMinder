package com.application.pillminderplus.medecinetasks.addingmedicine.fragments;

import android.os.Build;
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
import com.application.pillminderplus.medecinetasks.addingmedicine.MedicineInstruction;

public class InstructionsFragment extends Fragment implements View.OnClickListener {
    Bundle savedInstanceState;
    public InstructionsFragment() {
    }
    public static InstructionsFragment newInstance(String param1, String param2) {
        InstructionsFragment fragment = new InstructionsFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instructions, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        String toolbarTitle = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().getName();
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText(toolbarTitle);
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("Medication Instruction:");
        view.findViewById(R.id.text_view_before_eating_add_med_instructions).setOnClickListener(this);
        view.findViewById(R.id.text_view_while_eating_add_med_instructions).setOnClickListener(this);
        view.findViewById(R.id.text_view_after_eating_add_med_instructions).setOnClickListener(this);
    }
    // Saving chosen instruction and finish and add this medicine
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.text_view_before_eating_add_med_instructions) {
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setInstructions(MedicineInstruction.BEFORE_EATING.getInstruction());
        }
//        else if(v.getId() == R.id.text_view_while_eating_add_med_instructions) {
//            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setInstructions(MedicineInstruction.WHILE_EATING.getInstruction());
//        }
        else if(v.getId() == R.id.text_view_after_eating_add_med_instructions) {
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setInstructions(MedicineInstruction.AFTER_EATING.getInstruction());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((AddMedicineActivity) getActivity()).getAddMedPresenter().addMedFinished();
        }
    }
}