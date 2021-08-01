package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Map;

public class AttendanceActivity extends AppCompatActivity {

    // Variable and Views
    private SharedPreferences mStudentDb;
    private SharedPreferences mAttendanceDb;

    private ListView mStudentsList;
    private Button btnSave;
    private Button btnDatePicker;

    private StudentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // Initializing Variables
        mStudentDb = this.getSharedPreferences(MainActivity.STUDENTS, Context.MODE_PRIVATE);
        mAttendanceDb = this.getSharedPreferences(MainActivity.ATTENDANCE, Context.MODE_PRIVATE);
        mStudentsList = (ListView) findViewById(R.id.list_student);
        btnSave = (Button) findViewById(R.id.save_attendance);
        btnDatePicker = (Button) findViewById(R.id.btn_date);

        // Setting Click Listener for present/absent toggle
        mStudentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int item, long id) {
                StudentAdapter adapter1 = (StudentAdapter) parent.getAdapter();
                adapter1.setCheckedItem(item);
                ((CheckBox) view.findViewById(R.id.chk_student_present)).toggle();
            }
        });

        // Saving Data on Clicking save
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        // Showing DatePicker on Click
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        // Retrieving Student Data
        Map<String, String> students = (Map<String, String>) mStudentDb.getAll();

        // Only load attendance checkboxes if there are any students
        if (!students.isEmpty()) {
            loadStudents(students);
        }
    }

    // Loading Student list into list view
    private void loadStudents(Map<String, String> students) {
        // Loading data into the list
        mAdapter = new StudentAdapter(this, students.values().toArray(new String[0]));
        mStudentsList.setAdapter(mAdapter);
    }

    // Saving Attendance with date in SharedPreferences
    public void saveData() {
        // Get the date
        String date = btnDatePicker.getText().toString();

        // Validate if there is any data
        if (mAdapter != null && date != null && date != "Select Date") {
            // Join students name into comma separated string
            String students = TextUtils.join(",", mAdapter.getCheckedItems().values());

            // Save Data
            SharedPreferences.Editor editor = mAttendanceDb.edit();
            editor.putString(date, students);
            editor.apply();

            // Show user success toast and turn them back to main screen
            Toast.makeText(AttendanceActivity.this,
                    "Students: " + students + " were present on " + date,
                    Toast.LENGTH_SHORT).show();

            // Send user back to main screen
            Intent i = new Intent(AttendanceActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    // DatePicker for attendance
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        // Creating DatePicker instance
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Setting Date on the button
            Button btnDate = (Button) getActivity().findViewById(R.id.btn_date);
            btnDate.setText(day + "/" + (month + 1) + "/" + year);
        }
    }
}