package com.application.pillminderplus;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
//Helper for dialog and notifications
public class Helper {

    public static void showAlert(Context context, int title, String message, int icon) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {

                })
                .setIcon(icon)
                .show();
    }

    public static void openNotification(MedicineDose medicineDose, Medicine medicine, Context context, String title) {
        Notification notificationHelper = new Notification(context, medicine, medicineDose, title);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(medicineDose.getId().hashCode(), nb.build());
    }
}
