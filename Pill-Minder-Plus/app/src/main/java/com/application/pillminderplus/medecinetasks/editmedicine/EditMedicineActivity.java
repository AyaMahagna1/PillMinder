package com.application.pillminderplus.medecinetasks.editmedicine;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.application.pillminderplus.JSONSerializer;
import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.MedicineDayFrequency;
import com.application.pillminderplus.medecinetasks.addingmedicine.MedicineInstruction;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

//Edit medicine screen
public class EditMedicineActivity extends AppCompatActivity implements View.OnClickListener {
    EditMedicinePresenter editMedicinePresenter;
    ProgressDialog dialog;
    EditText nameEditText;
    Spinner spinner;
    Switch isActivatedSwitch;
    TextView firstDoseTimeTextView;
    TextView secondDoseTimeTextView;
    TextView thirdDoseTimeTextView;
    TextView fourthDoseTimeTextView;
    TextView firstDoseAmountTextView;
    TextView secondDoseAmountTextView;
    TextView thirdDoseAmountTextView;
    TextView fourthDoseAmountTextView;
    TextView startDateTextView;
    TextView endDateTextView;
    RadioButton everydayRadioButton;
    RadioButton everyNDayRadioButton;
    RadioButton specificDaysRadioButton;
    EditText reasonEditText;
    RadioButton beforeEatingRadioButton;
    RadioButton whileEatingRadioButton;
    RadioButton afterEatingRadioButton;
    Switch refillReinderSwitch;
    EditText remainingMedAmountEditText;
    EditText reminderMedAmountEditText;
    TextView refillReminderTimeTextView;
    Button submitButton;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);
        editMedicinePresenter = new EditMedicinePresenter(this);
        editMedicinePresenter.setMedicine((Medicine) getIntent().getSerializableExtra("medicine"));
        editMedicinePresenter.setDoses(JSONSerializer.deserializeMedicineDoses(getIntent().getStringExtra("doses")));
        initViewIDs();
        showProgressDialog();
        setUI();
    }

    private void initViewIDs() {
        nameEditText = findViewById(R.id.edit_text_med_name_edit_med);
        spinner = findViewById(R.id.spinner_reminder_times_edit_med);
        isActivatedSwitch = findViewById(R.id.switch_med_reminder_times_edit_med);
        firstDoseTimeTextView = findViewById(R.id.text_view_first_dose_time_edit_med);
        secondDoseTimeTextView = findViewById(R.id.text_view_second_dose_time_edit_med);
        thirdDoseTimeTextView = findViewById(R.id.text_view_third_dose_time_edit_med);
        fourthDoseTimeTextView = findViewById(R.id.text_view_fourth_dose_time_edit_med);
        firstDoseAmountTextView = findViewById(R.id.text_view_first_dose_amount_edit_med);
        secondDoseAmountTextView = findViewById(R.id.text_view_second_dose_amount_edit_med);
        thirdDoseAmountTextView = findViewById(R.id.text_view_third_dose_amount_edit_med);
        fourthDoseAmountTextView = findViewById(R.id.text_view_fourth_dose_amount_edit_med);
        startDateTextView = findViewById(R.id.text_view_start_date_edit_med);
        endDateTextView = findViewById(R.id.text_view_end_date_edit_med);
        everydayRadioButton = findViewById(R.id.radio_button_every_day_edit_med);
        everyNDayRadioButton = findViewById(R.id.radio_button_every_number_of_days_edit_med);
        specificDaysRadioButton = findViewById(R.id.radio_button_specific_days_of_the_week_edit_med);
        reasonEditText = findViewById(R.id.edit_text_reason_edit_med);
        beforeEatingRadioButton = findViewById(R.id.radio_button_before_eating_edit_med);
        whileEatingRadioButton = findViewById(R.id.radio_button_while_eating_edit_med);
        afterEatingRadioButton = findViewById(R.id.radio_button_after_eating_edit_med);
        refillReinderSwitch = findViewById(R.id.switch_refill_reminder_edit_med);
        remainingMedAmountEditText = findViewById(R.id.edit_text_remaining_med_amount_edit_med);
        reminderMedAmountEditText = findViewById(R.id.edit_text_reminder_amount_edit_med);
        refillReminderTimeTextView = findViewById(R.id.text_view_refill_time_chooser_edit_med);
        submitButton = findViewById(R.id.button_submit_edit_med);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setUI() {
        setNameCardViewUI();
        setReminderTimesCardViewUI();
        setScheduleCardViewUI();
        setReasonCardViewUI();
        setInstructionsCardViewUI();
        setRefillCardViewUI();
        setSubmitButton();
    }

    private void setNameCardViewUI() {
        nameEditText.setText(editMedicinePresenter.getMedicine().getName());
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    submitButton.setVisibility(View.GONE);
                } else {
                    submitButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isContaining(ArrayList<LocalTime> times, String time) {
        for (LocalTime t : times) {
            if (t.truncatedTo(ChronoUnit.MINUTES).toString().equals(LocalDateTime.parse(time).toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString())) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setReminderTimesCardViewUI() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time_frequency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(editMedicinePresenter.getMedicine().getTimeFrequency() - 1);
        spinner.setEnabled(false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                editMedicinePresenter.getMedicine().setTimeFrequency(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(editMedicinePresenter.getMedicine().getTimeFrequency() - 1);
            }
        });

        isActivatedSwitch.setChecked(editMedicinePresenter.getMedicine().getActivated());
        isActivatedSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMedicinePresenter.getMedicine().setActivated(((Switch) view).isChecked());
            }
        });

        ArrayList<LocalTime> times = new ArrayList<>();
        ArrayList<Integer> amounts = new ArrayList<>();

        for (MedicineDose dose : editMedicinePresenter.getDoses()) {
            if (times.size() == 0 || !isContaining(times, dose.getTime())) {
                times.add(LocalDateTime.parse(dose.getTime()).toLocalTime().truncatedTo(ChronoUnit.MINUTES));
                amounts.add(dose.getAmount());
            }
        }


        if (times.size() == 1) {
            firstDoseTimeTextView.setVisibility(View.VISIBLE);
            firstDoseAmountTextView.setVisibility(View.VISIBLE);
            firstDoseTimeTextView.setText(times.get(0).toString());
            firstDoseAmountTextView.setText(amounts.get(0).toString() + "Med(s)");
            secondDoseTimeTextView.setVisibility(View.GONE);
            secondDoseAmountTextView.setVisibility(View.GONE);
            thirdDoseTimeTextView.setVisibility(View.GONE);
            thirdDoseAmountTextView.setVisibility(View.GONE);
            fourthDoseTimeTextView.setVisibility(View.GONE);
            fourthDoseAmountTextView.setVisibility(View.GONE);
        } else if (times.size() == 2) {
            firstDoseTimeTextView.setVisibility(View.VISIBLE);
            firstDoseAmountTextView.setVisibility(View.VISIBLE);
            secondDoseTimeTextView.setVisibility(View.VISIBLE);
            secondDoseAmountTextView.setVisibility(View.VISIBLE);
            firstDoseTimeTextView.setText(times.get(0).toString());
            firstDoseAmountTextView.setText(amounts.get(0).toString() + " Med(s)");
            secondDoseTimeTextView.setText(times.get(1).toString());
            secondDoseAmountTextView.setText(amounts.get(1).toString() + " Med(s)");
            thirdDoseTimeTextView.setVisibility(View.GONE);
            thirdDoseAmountTextView.setVisibility(View.GONE);
            fourthDoseTimeTextView.setVisibility(View.GONE);
            fourthDoseAmountTextView.setVisibility(View.GONE);
        } else if (times.size() == 3) {
            firstDoseTimeTextView.setVisibility(View.VISIBLE);
            firstDoseAmountTextView.setVisibility(View.VISIBLE);
            secondDoseTimeTextView.setVisibility(View.VISIBLE);
            secondDoseAmountTextView.setVisibility(View.VISIBLE);
            thirdDoseTimeTextView.setVisibility(View.VISIBLE);
            thirdDoseAmountTextView.setVisibility(View.VISIBLE);
            firstDoseTimeTextView.setText(times.get(0).toString());
            firstDoseAmountTextView.setText(amounts.get(0).toString() + " Med(s)");
            secondDoseTimeTextView.setText(times.get(1).toString());
            secondDoseAmountTextView.setText(amounts.get(1).toString() + " Med(s)");
            thirdDoseTimeTextView.setText(times.get(2).toString());
            thirdDoseAmountTextView.setText(amounts.get(2).toString() + " Med(s)");
            fourthDoseTimeTextView.setVisibility(View.GONE);
            fourthDoseAmountTextView.setVisibility(View.GONE);
        } else if (times.size() == 4) {
            firstDoseTimeTextView.setVisibility(View.VISIBLE);
            firstDoseAmountTextView.setVisibility(View.VISIBLE);
            secondDoseTimeTextView.setVisibility(View.VISIBLE);
            secondDoseAmountTextView.setVisibility(View.VISIBLE);
            thirdDoseTimeTextView.setVisibility(View.VISIBLE);
            thirdDoseAmountTextView.setVisibility(View.VISIBLE);
            fourthDoseTimeTextView.setVisibility(View.VISIBLE);
            fourthDoseAmountTextView.setVisibility(View.VISIBLE);
            firstDoseTimeTextView.setText(times.get(0).toString());
            firstDoseAmountTextView.setText(amounts.get(0).toString() + " Med(s)");
            secondDoseTimeTextView.setText(times.get(1).toString());
            secondDoseAmountTextView.setText(amounts.get(1).toString() + " Med(s)");
            thirdDoseTimeTextView.setText(times.get(2).toString());
            thirdDoseAmountTextView.setText(amounts.get(2).toString() + " Med(s)");
            fourthDoseTimeTextView.setText(times.get(3).toString());
            fourthDoseAmountTextView.setText(amounts.get(3).toString() + " Med(s)");
        }

        firstDoseTimeTextView.setOnClickListener(this);
        secondDoseTimeTextView.setOnClickListener(this);
        thirdDoseTimeTextView.setOnClickListener(this);
        fourthDoseTimeTextView.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setScheduleCardViewUI() {
        findViewById(R.id.radio_group_days_edit_med).setEnabled(false);
        ((TextView) findViewById(R.id.text_view_start_date_edit_med)).setText(editMedicinePresenter.getMedicine().getStartDate());
        ((TextView) findViewById(R.id.text_view_end_date_edit_med)).setText(editMedicinePresenter.getMedicine().getEndDate());

        if (editMedicinePresenter.getMedicine().getDayFrequency().equals(MedicineDayFrequency.EVERYDAY.getFrequency())) {
            ((RadioButton) findViewById(R.id.radio_button_every_day_edit_med)).setChecked(true);
        } else if (editMedicinePresenter.getMedicine().getDayFrequency().equals(MedicineDayFrequency.EVERY_NUMBER_OF_DAYS.getFrequency())) {
            ((RadioButton) findViewById(R.id.radio_button_every_number_of_days_edit_med)).setChecked(true);
        } else if (editMedicinePresenter.getMedicine().getDayFrequency().equals(MedicineDayFrequency.SPECIFIC_DAYS.getFrequency())) {
            ((RadioButton) findViewById(R.id.radio_button_specific_days_of_the_week_edit_med)).setChecked(true);
        }

        ((RadioButton) findViewById(R.id.radio_button_every_day_edit_med)).setEnabled(false);
        ((RadioButton) findViewById(R.id.radio_button_every_number_of_days_edit_med)).setEnabled(false);
        ((RadioButton) findViewById(R.id.radio_button_specific_days_of_the_week_edit_med)).setEnabled(false);

    }

    private void setReasonCardViewUI() {
        reasonEditText.setText(editMedicinePresenter.getMedicine().getReason());

        reasonEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    submitButton.setVisibility(View.GONE);
                } else {
                    submitButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setInstructionsCardViewUI() {

        if (editMedicinePresenter.getMedicine().getInstructions().equals(MedicineInstruction.BEFORE_EATING.getInstruction())) {
            beforeEatingRadioButton.setChecked(true);
        } else if (editMedicinePresenter.getMedicine().getInstructions().equals(MedicineInstruction.WHILE_EATING.getInstruction())) {
            whileEatingRadioButton.setChecked(true);
        } else if (editMedicinePresenter.getMedicine().getInstructions().equals(MedicineInstruction.AFTER_EATING.getInstruction())) {
            afterEatingRadioButton.setChecked(true);
        }

        beforeEatingRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editMedicinePresenter.getMedicine().setInstructions(MedicineInstruction.BEFORE_EATING.getInstruction());
                }
            }
        });

        whileEatingRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editMedicinePresenter.getMedicine().setInstructions(MedicineInstruction.WHILE_EATING.getInstruction());
                }
            }
        });

        afterEatingRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editMedicinePresenter.getMedicine().setInstructions(MedicineInstruction.AFTER_EATING.getInstruction());
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setRefillCardViewUI() {

        refillReinderSwitch.setEnabled(false);

        remainingMedAmountEditText.setText(editMedicinePresenter.getMedicine().getRemainingMedAmount().toString());
        reminderMedAmountEditText.setText(editMedicinePresenter.getMedicine().getReminderMedAmount().toString());

        remainingMedAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    submitButton.setVisibility(View.GONE);
                } else {
                    submitButton.setVisibility(View.VISIBLE);
                }
            }
        });

        reminderMedAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    submitButton.setVisibility(View.GONE);
                } else {
                    submitButton.setVisibility(View.VISIBLE);
                }
            }
        });

        refillReminderTimeTextView.setText(LocalDateTime.parse(editMedicinePresenter.getMedicine().getRefillReminderTime()).toLocalTime().toString());

        refillReminderTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditMedicineActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialog = inflater.inflate(R.layout.refill_reminder_time_picker_dialog, null);
                TimePicker timePicker = dialog.findViewById(R.id.time_picker_refill_reminder_dialog);
                timePicker.setIs24HourView(true);


                timePicker.setHour(LocalDateTime.parse(editMedicinePresenter.getMedicine().getRefillReminderTime()).toLocalTime().getHour());
                timePicker.setMinute(LocalDateTime.parse(editMedicinePresenter.getMedicine().getRefillReminderTime()).toLocalTime().getMinute());

                builder.setView(dialog)
                        .setTitle("When do you need to be reminded to refill?")
                        .setPositiveButton("SET", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LocalDate date = LocalDateTime.parse(editMedicinePresenter.getMedicine().getRefillReminderTime()).toLocalDate();
                                LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(timePicker.getHour(), timePicker.getMinute()));

                                editMedicinePresenter.getMedicine().setRefillReminderTime(dateTime.toString());
                                setRefillCardViewUI();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();

            }
        });
    }

    private void setSubmitButton() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMedicinePresenter.getMedicine().setName(nameEditText.getText().toString());
                editMedicinePresenter.getMedicine().setRemainingMedAmount(Integer.parseInt(remainingMedAmountEditText.getText().toString()));
                editMedicinePresenter.getMedicine().setReminderMedAmount(Integer.parseInt(reminderMedAmountEditText.getText().toString()));
                editMedicinePresenter.getMedicine().setReason(reasonEditText.getText().toString());

                editMedicinePresenter.submitUpdates();
            }
        });
    }


    public Context getViewContext() {
        return this;
    }

    public void closeView() {
        finish();
    }

    public void showSuccessToast() {
        Toast.makeText(this, "Edited Successfully.", Toast.LENGTH_SHORT).show();
    }

    public void showFailureToast() {
        Toast.makeText(this, "Edit Failed. Try Again.", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialog = inflater.inflate(R.layout.edit_dose_dialog, null);
        TimePicker timePicker = dialog.findViewById(R.id.time_picker_edit_dose_dialog);
        timePicker.setIs24HourView(true);

        EditText amountEditText = dialog.findViewById(R.id.edit_text_amount_edit_dose_dialog);


        if (view.getId() == R.id.text_view_first_dose_time_edit_med) {
            buildDoseTimeAndAmountDialog(builder, dialog, timePicker, amountEditText, 0);
        } else if (view.getId() == R.id.text_view_second_dose_time_edit_med) {
            buildDoseTimeAndAmountDialog(builder, dialog, timePicker, amountEditText, 1);
        } else if (view.getId() == R.id.text_view_third_dose_time_edit_med) {
            buildDoseTimeAndAmountDialog(builder, dialog, timePicker, amountEditText, 2);
        } else if (view.getId() == R.id.text_view_fourth_dose_time_edit_med) {
            buildDoseTimeAndAmountDialog(builder, dialog, timePicker, amountEditText, 3);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildDoseTimeAndAmountDialog(AlertDialog.Builder builder, View dialog, TimePicker timePicker, EditText amountEditText, int doseRank) {
        timePicker.setHour(LocalDateTime.parse(editMedicinePresenter.getDoses().get(doseRank).getTime()).toLocalTime().getHour());
        timePicker.setMinute(LocalDateTime.parse(editMedicinePresenter.getDoses().get(doseRank).getTime()).toLocalTime().getMinute());
        amountEditText.setText(editMedicinePresenter.getDoses().get(doseRank).getAmount().toString());

        builder.setView(dialog)
                .setTitle("When do you need to take this dose?")
                .setPositiveButton("SET", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        for (int j = 0; (j + doseRank) < editMedicinePresenter.getDoses().size(); j += editMedicinePresenter.getMedicine().getTimeFrequency()) {
                            LocalDate date = LocalDateTime.parse(editMedicinePresenter.getDoses().get(doseRank + j).getTime()).toLocalDate();
                            LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(timePicker.getHour(), timePicker.getMinute()));
                            editMedicinePresenter.getDoses().get(doseRank + j).setTime(dateTime.toString());
                        }


                        Integer amount;
                        if (amountEditText.getText().toString().equals("")) {
                            amount = editMedicinePresenter.getDoses().get(doseRank).getAmount();
                        } else {
                            amount = Integer.parseInt(amountEditText.getText().toString());
                            if (amount == 0) {
                                amount = editMedicinePresenter.getDoses().get(doseRank).getAmount();
                            }
                        }
                        for (int j = 0; (j + doseRank) < editMedicinePresenter.getDoses().size(); j += editMedicinePresenter.getMedicine().getTimeFrequency()) {
                            editMedicinePresenter.getDoses().get(doseRank + j).setAmount(amount);
                        }
                        setReminderTimesCardViewUI();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }

    public void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.show();
    }

    public void hideProgressDialog() {
        dialog.dismiss();
    }
}