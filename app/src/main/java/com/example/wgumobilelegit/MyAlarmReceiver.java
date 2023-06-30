package com.example.wgumobilelegit;

// Import necessary Android and Java libraries
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

// Define the BroadcastReceiver class
public class MyAlarmReceiver extends BroadcastReceiver {
    // Define the notification ID
    private static final int NOTIFICATION_ID = 1;

    // Override the onReceive method
    @Override
    public void onReceive(Context context, Intent intent) {

        int TypeNotifcation = intent.getIntExtra("Type", 0);
        // Create a NotificationManager object
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the Android version is Oreo or higher
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel object
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT);
            // Create the notification channel
            notificationManager.createNotificationChannel(channel);
        }

        if(TypeNotifcation == 0){

            Log.d("Troubleshoot", "Result Code"+getResultCode());
            // Create a NotificationCompat.Builder object
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Start Date Reminder")
                    .setContentText("Your Course Starts Today!")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);

            // Notify the user with the built notification
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else if (TypeNotifcation == 1)
        {
            Log.d("Troubleshoot", "Result Code"+getResultCode());
            // Create a NotificationCompat.Builder object
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("End Date Reminder")
                    .setContentText("Your Course Ends Today!")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);

            // Notify the user with the built notification
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else if (TypeNotifcation == 2) {
            Log.d("Troubleshoot", "Result Code"+getResultCode());
            // Create a NotificationCompat.Builder object
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Start Date Reminder")
                    .setContentText("Your Assessment Starts Today!")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);

            // Notify the user with the built notification
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        } else if (TypeNotifcation == 3) {
            Log.d("Troubleshoot", "Result Code"+getResultCode());
            // Create a NotificationCompat.Builder object
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("End Date Reminder")
                    .setContentText("Your Assessment Ends Today!")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true);

            // Notify the user with the built notification
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}
