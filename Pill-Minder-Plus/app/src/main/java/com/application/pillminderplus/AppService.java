package com.application.pillminderplus;

import static com.application.pillminderplus.Notification.CHANNEL_DESCRIPTION;
import static com.application.pillminderplus.Notification.CHANNEL_ID;
import static com.application.pillminderplus.Notification.CHANNEL_NAME;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.application.pillminderplus.home.HomeActivity;
import com.application.pillminderplus.medicinereminder.MedicationReminderActivity;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
//Service for starting the app and its notifications
public class AppService extends Service {
    public static final int CH_ID = 101;
    public AppService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        buildNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Medicine medicine = (Medicine) intent.getSerializableExtra(Constants.MEDICINE_KEY);
        MedicineDose dose = (MedicineDose) intent.getSerializableExtra(Constants.DOSE_KEY);
        Helper.openNotification(dose, medicine, this, getApplicationContext().getString(R.string.medicine_time));
        Intent startIntent = new Intent(this, MedicationReminderActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        startIntent.putExtra(Constants.MEDICINE_KEY, medicine);
        startIntent.putExtra(Constants.DOSE_KEY, dose);
        startActivity(startIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the Notification Channel class is new and not in the support library
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);

            // Register the channel with the system;
            // you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void buildNotification() {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.do_not_forgot_medicine))
                .setContentText(getString(R.string.appointed_time))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        startForeground(CH_ID, notification);
    }
}