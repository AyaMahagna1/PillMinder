package com.application.pillminderplus.medecinetasks.displaymedicine;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;

import com.application.pillminderplus.JSONSerializer;
import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.MedicineDayFrequency;
import com.application.pillminderplus.medecinetasks.addingmedicine.MedicineForm;
import com.application.pillminderplus.medecinetasks.editmedicine.EditMedicineActivity;
import com.application.pillminderplus.model.DoseStatus;
import com.application.pillminderplus.model.MedicineDose;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;
//The screen for showing the medicine details
public class DisplayMedicineFragment extends Fragment {
    DisplayMedicinePresenter displayMedicinePresenter;
    View view;
    ProgressDialog dialog;

    public DisplayMedicineFragment() {

    }
    public static DisplayMedicineFragment newInstance() {
        return new DisplayMedicineFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        setPresenter();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_display_medicine, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        this.view = view;
        dialog = new ProgressDialog(getContext());
        dialog.show();
        setPresenter();
    }

    private void setPresenter() {
        displayMedicinePresenter = new DisplayMedicinePresenter(this);
        displayMedicinePresenter.getStoredMedicineAndDoses(Objects.requireNonNull(getArguments()).getString("medicineID"));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUI() {

        if (displayMedicinePresenter.getMedicine().getForm().equals(MedicineForm.PILLS.getForm())) {
            ((ImageView) view.findViewById(R.id.image_view_med_icon_display_med)).setImageResource(R.drawable.ic_pillsgif);
        } else if (displayMedicinePresenter.getMedicine().getForm().equals(MedicineForm.SOLUTION.getForm())) {
            ((ImageView) view.findViewById(R.id.image_view_med_icon_display_med)).setImageResource(R.drawable.ic_solution);
        } else if (displayMedicinePresenter.getMedicine().getForm().equals(MedicineForm.DROPS.getForm())) {
            ((ImageView) view.findViewById(R.id.image_view_med_icon_display_med)).setImageResource(R.drawable.ic_drops);
        } else if (displayMedicinePresenter.getMedicine().getForm().equals(MedicineForm.INHALER.getForm())) {
            ((ImageView) view.findViewById(R.id.image_view_med_icon_display_med)).setImageResource(R.drawable.ic_inhaler);
        } else if (displayMedicinePresenter.getMedicine().getForm().equals(MedicineForm.TOPICAL.getForm())) {
            ((ImageView) view.findViewById(R.id.image_view_med_icon_display_med)).setImageResource(R.drawable.ic_topical);
        } else if (displayMedicinePresenter.getMedicine().getForm().equals(MedicineForm.POWDER.getForm())) {
            ((ImageView) view.findViewById(R.id.image_view_med_icon_display_med)).setImageResource(R.drawable.ic_powder);
        } else {
            ((ImageView) view.findViewById(R.id.image_view_med_icon_display_med)).setImageResource(R.drawable.ic_injection);
        }

        view.findViewById(R.id.icon_edit_display_med_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditMedicineActivity.class);
                intent.putExtra("medicine", displayMedicinePresenter.getMedicine());
                intent.putExtra("doses", JSONSerializer.serializeMedicineDoses(displayMedicinePresenter.getDoses()));
                startActivity(intent);
            }
        });

        view.findViewById(R.id.icon_delete_display_med_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(getContext()))
                        .setMessage("Are you sure?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                displayMedicinePresenter.deleteMedicine();
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
            }
        });

        ((TextView) view.findViewById(R.id.text_view_med_name_display_med)).setText(displayMedicinePresenter.getMedicine().getName());
        ((TextView) view.findViewById(R.id.text_view_strength_display_med)).setText("");

        if (displayMedicinePresenter.getMedicine().getDayFrequency().equals(MedicineDayFrequency.EVERYDAY.getFrequency())) {
            ((TextView) view.findViewById(R.id.text_view_day_frequency_display_med)).setText("Everyday");
        } else if (displayMedicinePresenter.getMedicine().getDayFrequency().equals(MedicineDayFrequency.EVERY_NUMBER_OF_DAYS.getFrequency())) {
            ((TextView) view.findViewById(R.id.text_view_day_frequency_display_med)).setText("Every " + displayMedicinePresenter.getMedicine().getEveryNDays() + " days");
        } else {
            ((TextView) view.findViewById(R.id.text_view_day_frequency_display_med)).setText(displayMedicinePresenter.getMedicine().getWeekDays());
        }

        ((TextView) view.findViewById(R.id.text_view_remaining_amount_display_med)).setText(displayMedicinePresenter.getMedicine().getRemainingMedAmount() + "");
        ((TextView) view.findViewById(R.id.text_view_remind_count_display_med)).setText(displayMedicinePresenter.getMedicine().getReminderMedAmount() + "");

        if (displayMedicinePresenter.getDoses().size() != 0) {
            setLastTakenTextView(view, displayMedicinePresenter.getDoses());
            setTimesTextViews(view, displayMedicinePresenter.getDoses());
            setAmountsTextViews(view, displayMedicinePresenter.getDoses());
        }

        Button suspendButton = view.findViewById(R.id.button_suspend_display_med);
        Button refillButton = view.findViewById(R.id.button_refill_display_med);

        if (displayMedicinePresenter.getMedicine().getActivated()) {
            suspendButton.setText("Suspend");
        } else {
            suspendButton.setText("Activate");
        }
        suspendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (displayMedicinePresenter.getMedicine().getActivated()) {
                    suspendButton.setText("Activate");
                    displayMedicinePresenter.getMedicine().setActivated(false);
                    displayMedicinePresenter.removeOneTimeWorkRequest();
                } else {
                    suspendButton.setText("Suspend");
                    displayMedicinePresenter.getMedicine().setActivated(true);
                    displayMedicinePresenter.addOneTimeWorkRequest();
                }
                displayMedicinePresenter.updateMedicine();
            }
        });

        refillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialog = inflater.inflate(R.layout.refill_amount_dialog, null);
                builder.setView(dialog)
                        .setTitle("Refill")
                        .setMessage("You have " + displayMedicinePresenter.getMedicine().getRemainingMedAmount() + " meds remaining")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                displayMedicinePresenter.getMedicine().setRemainingMedAmount(
                                        displayMedicinePresenter.getMedicine().getRemainingMedAmount() +
                                                Integer.parseInt(((EditText) dialog.findViewById(R.id.edit_text_add_amount_refill_amount_dialog)).getText().toString())
                                );
                                displayMedicinePresenter.updateMedicine();
                                setUI();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void setLastTakenTextView(@NonNull View view, ArrayList<MedicineDose> doses) {
        String lastTakenString = "Unknown";
        for (int i = doses.size() - 1; i >= 0; i--) {
            if (doses.get(i).getStatus().equals(DoseStatus.TAKEN.getStatus())) {
                lastTakenString = doses.get(i).getTime();
                break;
            }
        }

        ((TextView) view.findViewById(R.id.text_view_last_taken_display_med)).setText(lastTakenString);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setTimesTextViews(@NonNull View view, ArrayList<MedicineDose> doses) {
        ArrayList<String> dosesTimes = new ArrayList<>();
        String firstDay = LocalDateTime.parse(doses.get(0).getTime()).toLocalDate().toString();
        for (MedicineDose dose : doses) {
            if (firstDay.equals(LocalDateTime.parse(dose.getTime()).toLocalDate().toString())) {
                dosesTimes.add(LocalDateTime.parse(dose.getTime()).toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString());
            }
        }


        if (dosesTimes.size() == 1) {
            view.findViewById(R.id.text_view_time_1_display_med).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text_view_time_1_display_med)).setText(dosesTimes.get(0));
            view.findViewById(R.id.text_view_time_2_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_time_3_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_time_4_display_med).setVisibility(View.GONE);
        } else if (dosesTimes.size() == 2) {
            view.findViewById(R.id.text_view_time_1_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_time_2_display_med).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text_view_time_1_display_med)).setText(dosesTimes.get(0));
            ((TextView) view.findViewById(R.id.text_view_time_2_display_med)).setText(dosesTimes.get(1));
            view.findViewById(R.id.text_view_time_3_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_time_4_display_med).setVisibility(View.GONE);
        } else if (dosesTimes.size() == 3) {
            view.findViewById(R.id.text_view_time_1_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_time_2_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_time_3_display_med).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text_view_time_1_display_med)).setText(dosesTimes.get(0));
            ((TextView) view.findViewById(R.id.text_view_time_2_display_med)).setText(dosesTimes.get(1));
            ((TextView) view.findViewById(R.id.text_view_time_3_display_med)).setText(dosesTimes.get(2));
            view.findViewById(R.id.text_view_time_4_display_med).setVisibility(View.GONE);
        } else if (dosesTimes.size() == 4) {
            view.findViewById(R.id.text_view_time_1_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_time_2_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_time_3_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_time_4_display_med).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text_view_time_1_display_med)).setText(dosesTimes.get(0));
            ((TextView) view.findViewById(R.id.text_view_time_2_display_med)).setText(dosesTimes.get(1));
            ((TextView) view.findViewById(R.id.text_view_time_3_display_med)).setText(dosesTimes.get(2));
            ((TextView) view.findViewById(R.id.text_view_time_4_display_med)).setText(dosesTimes.get(3));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAmountsTextViews(@NonNull View view, ArrayList<MedicineDose> doses) {
        ArrayList<Integer> dosesAmounts = new ArrayList<>();
        String firstDay = LocalDateTime.parse(doses.get(0).getTime()).toLocalDate().toString();
        for (MedicineDose dose : doses) {
            if (firstDay.equals(LocalDateTime.parse(dose.getTime()).toLocalDate().toString())) {
                dosesAmounts.add(dose.getAmount());
            }
        }

        if (dosesAmounts.size() == 1) {
            view.findViewById(R.id.text_view_dose_amount_1_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_take_1_display_med).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text_view_dose_amount_1_display_med)).setText(dosesAmounts.get(0) + "");
            view.findViewById(R.id.text_view_dose_amount_2_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_dose_amount_3_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_dose_amount_4_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_take_2_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_take_3_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_take_4_display_med).setVisibility(View.GONE);
        } else if (dosesAmounts.size() == 2) {
            view.findViewById(R.id.text_view_dose_amount_1_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_take_1_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_dose_amount_2_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_take_2_display_med).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text_view_dose_amount_1_display_med)).setText(dosesAmounts.get(0) + "");
            ((TextView) view.findViewById(R.id.text_view_dose_amount_2_display_med)).setText(dosesAmounts.get(1) + "");
            view.findViewById(R.id.text_view_dose_amount_3_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_dose_amount_4_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_take_3_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_take_4_display_med).setVisibility(View.GONE);
        } else if (dosesAmounts.size() == 3) {
            view.findViewById(R.id.text_view_dose_amount_1_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_take_1_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_dose_amount_2_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_take_2_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_dose_amount_3_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_take_3_display_med).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text_view_dose_amount_1_display_med)).setText(dosesAmounts.get(0) + "");
            ((TextView) view.findViewById(R.id.text_view_dose_amount_2_display_med)).setText(dosesAmounts.get(1) + "");
            ((TextView) view.findViewById(R.id.text_view_dose_amount_3_display_med)).setText(dosesAmounts.get(2) + "");
            view.findViewById(R.id.text_view_dose_amount_4_display_med).setVisibility(View.GONE);
            view.findViewById(R.id.text_view_take_4_display_med).setVisibility(View.GONE);
        } else if (dosesAmounts.size() == 4) {
            view.findViewById(R.id.text_view_dose_amount_1_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_take_1_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_dose_amount_2_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_take_2_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_dose_amount_3_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_take_3_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_dose_amount_4_display_med).setVisibility(View.VISIBLE);
            view.findViewById(R.id.text_view_take_4_display_med).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text_view_dose_amount_1_display_med)).setText(dosesAmounts.get(0) + "");
            ((TextView) view.findViewById(R.id.text_view_dose_amount_2_display_med)).setText(dosesAmounts.get(1) + "");
            ((TextView) view.findViewById(R.id.text_view_dose_amount_3_display_med)).setText(dosesAmounts.get(2) + "");
            ((TextView) view.findViewById(R.id.text_view_dose_amount_4_display_med)).setText(dosesAmounts.get(3) + "");
        }
    }


    public Context getViewContext() {
        return getContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshView() {
        setUI();
    }

    public void showProgressBar() {
        dialog.show();
    }

    public void hideProgressBar() {
        dialog.dismiss();
    }

    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void closeView() {
        Navigation.findNavController(view).popBackStack();
    }

    public LifecycleOwner getViewLifecycle() {
        return getViewLifecycleOwner();
    }
}