package com.example.sem_4_lab_4;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.sem_4_lab_4.NotificationData.notificationEventStarted;

public class Widget extends AppWidgetProvider {
    static String LOG_TAG = "myLogs";

    private Timer timer = new Timer();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetsIds){
        super.onUpdate(context, appWidgetManager, appWidgetsIds);

        //обновляем все экземпляры
        for (int i : appWidgetsIds){
            startTimeUpdate(context, appWidgetManager, appWidgetsIds);
            updateWidget(context, appWidgetManager, i);
        }
    }

    private void startTimeUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetsIds) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < appWidgetsIds.length; i++){
                    updateWidget(context, appWidgetManager, appWidgetsIds[i]);
                }
            }
        }, 0, 10000);
    }

    public void onDeleted(Context context, int[] appWidgetsIds){
        super.onDeleted(context, appWidgetsIds);

        //удаляем Preferences
        SharedPreferences.Editor editor = context.getSharedPreferences(ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();

        timer.cancel();
        for (int widgetsID : appWidgetsIds){
            editor.remove(ConfigActivity.WIDGET_DATE + widgetsID);
        }

        editor.commit();
    }

    static void updateWidget(Context ctx, AppWidgetManager appWidgetManager, int widgetID){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);

        int widgetDay = sharedPreferences.getInt(ConfigActivity.WIDGET_DATE + widgetID, 0);
        int widgetMonth = sharedPreferences.getInt(ConfigActivity.WIDGET_MONTH + widgetID, 0);
        int widgetYear = sharedPreferences.getInt(ConfigActivity.WIDGET_YEAR + widgetID, 0);

        int day_now, month_now, year_now;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date_now = new Date();
        String date = simpleDateFormat.format(date_now);
        String[] str;
        String delimeter = "-";
        str = date.split(delimeter);

        day_now = Integer.parseInt(str[0]);
        month_now = Integer.parseInt(str[1]);
        year_now = Integer.parseInt(str[2]);

        Date future = new Date(widgetYear, widgetMonth, widgetDay);
        Date now = new Date(year_now, month_now, day_now);

        int days = Days.daysBetween(new DateTime(now), new DateTime(future)).getDays();
        System.out.println(days);

        if (days == 0) {
            SimpleDateFormat sp = new SimpleDateFormat("HH:mm");
            Date date1 = new Date();
            String date_now_clock = sp.format(date1);
            System.out.println(date_now_clock);

            String[] hour_str;
            String del = ":";
            hour_str = date_now_clock.split(del);

            int hour = Integer.parseInt(hour_str[0]);

            if (hour >= 9){
                notificationEventStarted(ctx, widgetID);
            }
        }
        //помещаем данные в текстовые поля
        RemoteViews widgetView = new RemoteViews(ctx.getPackageName(), R.layout.widget);

        String widgetText = ""+days;
        widgetView.setTextViewText(R.id.tvTime, widgetText);
        widgetView.setTextViewText(R.id.tvDate, widgetDay + "-" + widgetMonth + "-" + widgetYear);

        //конфигурируем кнопку
        Intent configIntent = new Intent(ctx, ConfigActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, widgetID, configIntent, 0);
        widgetView.setOnClickPendingIntent(R.id.tvTime, pendingIntent);

        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }
}
