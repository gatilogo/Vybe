package com.example.vybe.AddEdit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vybe.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFieldFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText dateTimeField;
    private DateTimeSelectedListener dateTimeSelectedListener;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private Context context;

    interface DateTimeSelectedListener {
        void onDateTimeSelected(LocalDateTime localDateTime);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        Activity activity = (Activity) context;
        dateTimeSelectedListener = (DateTimeSelectedListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_time_field_fragment, container, false);

        dateTimeField = view.findViewById(R.id.date_time_edit_text);
        dateTimeField.setOnClickListener(v -> {
            openDatePickerDialog(selectedDate);
        });

        return view;
    }

    public void setDefaultDateTime(Bundle args) {
        if (args.containsKey("dateTime")) {
            LocalDateTime selectedDateTime = (LocalDateTime) args.getSerializable("dateTime");
            selectedDate = selectedDateTime.toLocalDate();
            selectedTime = selectedDateTime.toLocalTime();

            dateTimeField.setText(formatDateTime(selectedDateTime));
        }
    }

    private void openDatePickerDialog(LocalDate currDate) {
        int currYear = currDate.getYear();
        int currMonth = currDate.getMonthValue() - 1; // Since indexing starts at 0
        int currDay = currDate.getDayOfMonth();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, DateTimeFieldFragment.this, currYear, currMonth, currDay);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month += 1; // since indexing starts at 0
        selectedDate = LocalDate.of(year, month, day);

        openTimePickerDialog(selectedTime);
    }

    private void openTimePickerDialog(LocalTime currTime) {
        int currHour = currTime.getHour();
        int currMin = currTime.getMinute();

        TimePickerDialog tpd = new TimePickerDialog(getContext(), DateTimeFieldFragment.this, currHour, currMin, true);
        tpd.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        selectedTime = LocalTime.of(hour, min);

        LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, selectedTime);

        dateTimeField.setText(formatDateTime(selectedDateTime));
        dateTimeSelectedListener.onDateTimeSelected(selectedDateTime);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm a"));
    }
}
