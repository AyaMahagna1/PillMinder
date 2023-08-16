package com.application.pillminderplus.medecinetasks.addingmedicine.fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddMedicineActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
//Adding the medicine remaining amount and when to remind the user to refill
public class RefillReminderFragment extends Fragment {
    EditText remainingAmountEditText;
    EditText refillAmountEditText;
    LocalDateTime time;
    public RefillReminderFragment() {
    }
    public static RefillReminderFragment newInstance() {
        RefillReminderFragment fragment = new RefillReminderFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refill_reminder, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String toolbarTitle = ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().getName();
        ((TextView) view.findViewById(R.id.text_view_toolbar_title)).setText(toolbarTitle);
        ((TextView) view.findViewById(R.id.text_view_add_header)).setText("Medication Refill Reminder:");
        remainingAmountEditText = view.findViewById(R.id.edit_text_remaining_amount_refill_reminder_add_med);
        refillAmountEditText = view.findViewById(R.id.edit_text_remind_me_when_refill_reminder_add_med);
        TimePicker timePicker = view.findViewById(R.id.time_picker_refill_reminder_add_med);
        Button nextButton = view.findViewById(R.id.button_submit_edit_med);
        nextButton.setVisibility(View.GONE);
        timePicker.setIs24HourView(true);
        remainingAmountEditText.addTextChangedListener(new TextWatcher() {
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
                if(isAmountValid()) {
                    nextButton.setVisibility(View.VISIBLE);
                }
                else {
                    nextButton.setVisibility(View.GONE);
                }
            }
        });
        refillAmountEditText.addTextChangedListener(new TextWatcher() {
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
                if(isAmountValid()) {
                    nextButton.setVisibility(View.VISIBLE);
                }
                else {
                    nextButton.setVisibility(View.GONE);
                }
            }
        }); // Every thing was filled and chosen -> save and go to next fragment
        nextButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int remainingAmount = Integer.parseInt(remainingAmountEditText.getText().toString());
                int refillAmount = Integer.parseInt(refillAmountEditText.getText().toString());
                time = LocalDateTime.of(LocalDate.now(), LocalTime.of(timePicker.getHour(), timePicker.getMinute(), 0));
                ((AddMedicineActivity) getActivity()).closeKeyboard(view);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setRemainingMedAmount(remainingAmount);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setReminderMedAmount(refillAmount);
                ((AddMedicineActivity) getActivity()).getAddMedPresenter().getMedicine().setRefillReminderTime(time.toString());
                ((AddMedicineActivity) getActivity()).nextStep(savedInstanceState, new InstructionsFragment());
            }
        });

    }
    private boolean isAmountValid() {
        if(remainingAmountEditText.getText().toString().length() != 0 &&
                refillAmountEditText.getText().toString().length() != 0) {
            return true;
        }
        return false;
    }
}