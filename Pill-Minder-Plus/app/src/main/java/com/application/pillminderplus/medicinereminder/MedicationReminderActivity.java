package com.application.pillminderplus.medicinereminder;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.application.pillminderplus.Constants;
import com.application.pillminderplus.R;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.database.room.user.LocalSourceUser;
import com.application.pillminderplus.model.DoseStatus;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.model.User;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;

import de.hdodenhof.circleimageview.CircleImageView;
//Activity screen for showing the notification / reminder
public class MedicationReminderActivity extends AppCompatActivity  {
    MediaPlayer mediaPlayerSong;
    CircleImageView profile_image;
    TextView txtViewPersonalDescription, txtViewDoseTime, txtViewDoseName, txtViewDoseDescription;
    CardView cardViewSkip, cardViewTake, cardViewSnooze;
    Medicine medicine;
    MedicineDose dose;
    LocalDateTime dateTime;

    MedicineReminderPresenter presenter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_reminder);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(false);
        initComponents();
        presenter.getUserFromRoom(this);
        displayCardInfo();
        cardViewSkip.setOnClickListener(view -> updateDose(DoseStatus.SKIPPED.getStatus()));
        cardViewTake.setOnClickListener(view -> {
            medicine.setRemainingMedAmount(medicine.getRemainingMedAmount() - dose.getAmount());
            if (medicine.getRemainingMedAmount() <= medicine.getReminderMedAmount()) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this)
                        .setTitle(R.string.warning)
                        .setMessage(getString(R.string.refill) + medicine.getRemainingMedAmount())
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {

                        })
                        .setIcon(R.drawable.ic_warning)
                        .show();
            }
            medicine.setSync(false);
            updateDose(DoseStatus.TAKEN.getStatus());
        });
        cardViewSnooze.setOnClickListener(view -> {
            mediaPlayerSong.stop();
            dose.setStatus(DoseStatus.UNKNOWN.getStatus());
            dose.setSync(false);

            new Thread(() -> {
                // this is executed on another Thread
                presenter.snoozeDose(dose, medicine, this);

                // create a Handler associated with the main Thread
                Handler handler = new Handler(Looper.getMainLooper());
                // post a Runnable to the main Thread
                // this is executed on the main Thread
                handler.post(() -> {
                    Toast.makeText(this, R.string.snooze_for_five_minutes, Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDose(String status) {
        mediaPlayerSong.stop();
        dose.setStatus(status);
        dose.setSync(false);
        new Thread(() -> {
            // this is executed on another Thread
            presenter.updateDose(dose, medicine,MedicationReminderActivity.this);
            // create a Handler associated with the main Thread
            Handler handler = new Handler(Looper.getMainLooper());
            // post a Runnable to the main Thread
            // this is executed on the main Thread
            handler.post(MedicationReminderActivity.this::finish);
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initComponents() {
        presenter = new MedicineReminderPresenter(this,
                MedicineReminderRepository.getInstance(LocalSourceUser.getInstance(this), LocalSourceMedicineDose.getInstance(this)));
        profile_image = findViewById(R.id.profile_image);
        txtViewPersonalDescription = findViewById(R.id.txtViewPersonalDescription);
        txtViewDoseTime = findViewById(R.id.txtViewDoseTime);
        txtViewDoseName = findViewById(R.id.txtViewDoseName);
        txtViewDoseDescription = findViewById(R.id.txtViewDoseDescription);
        cardViewSkip = findViewById(R.id.cardViewSkip);
        cardViewTake = findViewById(R.id.cardViewTake);
        cardViewSnooze = findViewById(R.id.cardViewSnooze);
        medicine = (Medicine) getIntent().getSerializableExtra(Constants.MEDICINE_KEY);
        dose = (MedicineDose) getIntent().getSerializableExtra(Constants.DOSE_KEY);
        dateTime = LocalDateTime.parse(dose.getTime());
        mediaPlayerSong = MediaPlayer.create(this, R.raw.clockalarm);
        mediaPlayerSong.setLooping(true);
        mediaPlayerSong.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void displayUserInformation(User user) {

        Picasso.with(this).load(user.getProfile_image_uri()).into(profile_image);
        txtViewPersonalDescription.setText(getString(R.string.hello).concat(" ")
                .concat(user.getUsername()).concat(", ")
                .concat(getString(R.string.time_to_take)).concat(" ")
                .concat(dateTime.getHour() + ":" + dateTime.getMinute()).concat(" ")
                .concat(getString(R.string.meds)));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayCardInfo() {
        txtViewDoseTime.setText(String.valueOf(dateTime.getHour()).concat(":").concat(String.valueOf(dateTime.getMinute())));
        txtViewDoseName.setText(medicine.getName());
        txtViewDoseDescription.setText(String.valueOf(getString(R.string.take)).concat(" ")
                .concat(String.valueOf(dose.getAmount())).concat(" ")
                .concat(getString(R.string.before_eating)).concat(" ").concat(dose.getTime()));
    }
}