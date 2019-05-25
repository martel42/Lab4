package com.example.sem_4_lab_4;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationData {

    public static void notificationEventStarted(Context context, int widgetID){
        final int NOTIFY_ID = 1;

        Resources res = context.getResources();
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("EventStarted", "Установленная дата", NotificationManager.IMPORTANCE_DEFAULT);
        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "EventStarted")
                .setSmallIcon(R.drawable.ic_event_available)
                .setContentTitle("Уведомление")
                .setContentText("Сегодня день установленного события виджета № = " + widgetID)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_android))
                .setTicker("Done!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);

        notificationManager.notify(NOTIFY_ID, builder.build());

    }
}
