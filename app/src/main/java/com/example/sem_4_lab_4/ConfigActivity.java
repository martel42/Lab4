package com.example.sem_4_lab_4;

import android.app.DatePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConfigActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;

    final String LOG_TAG = "myLogs";

    public static final String WIDGET_PREF = "widget_pref";
    public static final String WIDGET_DATE = "widget_date";
    public static final String WIDGET_MONTH = "widget_month";
    public static final String WIDGET_YEAR = "widget_year";

    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Log.d(LOG_TAG, "onCreate config");

        //получаем ID конфигурируемого виджета
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null){
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        //проверка корректности
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }

        //формируем Intent ответ
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        //отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.config);

        DialogFragment datePicker = new com.example.sem_4_lab_4.DatePicker();
        datePicker.show(getSupportFragmentManager(), "date picker");

    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date_now = new Date();
        String date = simpleDateFormat.format(c.getTime());
        String[] str;
        String delimeter = "-";
        str = date.split(delimeter);

        int day_now = Integer.parseInt(str[0]);
        int month_now = Integer.parseInt(str[1]);
        int year_now = Integer.parseInt(str[2]);

        SharedPreferences sharedPreferences = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(WIDGET_DATE + widgetID, day_now);
        editor.putInt(WIDGET_MONTH + widgetID, month_now);
        editor.putInt(WIDGET_YEAR + widgetID, year_now);

        editor.commit();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        Widget.updateWidget(this, appWidgetManager, widgetID);

        setResult(RESULT_OK, resultValue);
        finish();
    }
}
