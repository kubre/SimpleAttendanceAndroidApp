package com.example.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Required variable and views
    private SharedPreferences mStudentDb;
    private SharedPreferences mAttendanceDb;

    private Button mBtnAddStudent;
    private Button mBtnAttendance;
    private EditText mTxtName;
    private TextView mLblStudents;
    private TextView mLblAttendance;

    // Constants for SharedPreferences file names
    public static final String STUDENTS = "com.example.attendanceapp.STUDENTS";
    public static final String ATTENDANCE = "com.example.attendanceapp.ATTENDANCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing shared preferences
        mStudentDb = this.getSharedPreferences(STUDENTS, Context.MODE_PRIVATE);
        mAttendanceDb = this.getSharedPreferences(ATTENDANCE, Context.MODE_PRIVATE);

        // Initializing views
        mBtnAddStudent = (Button) findViewById(R.id.btn_go_add);
        mBtnAttendance = (Button) findViewById(R.id.btn_go_view);
        mTxtName = (EditText) findViewById(R.id.txt_student);
        mLblStudents = (TextView) findViewById(R.id.lbl_students);
        mLblAttendance = (TextView) findViewById(R.id.lbl_attendance);

        // Printing Student Data
        Map<String, String> students = (Map<String, String>) mStudentDb.getAll();
        if(!students.isEmpty()) {
            mLblStudents.setText(TextUtils.join(", ", students.values()));
        }

        // Printing Attendance
        Map<String, String> attendances = (Map<String, String>) mAttendanceDb.getAll();
        if(!attendances.isEmpty()) {
            StringBuilder attendanceString = new StringBuilder("Date: Students Present\n\n");
            // Iterating over all available data
            for (Map.Entry<String, String> entry : attendances.entrySet()) {
                attendanceString.append(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            mLblAttendance.setText(attendanceString.toString());
        }

        // Adding student name to the records
        mBtnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mTxtName.getText().toString();

                if (name.isEmpty()) {
                    // Show user validation error as name was empty
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    // Save Student Data
                    SharedPreferences.Editor editor = mStudentDb.edit();
                    editor.putString(name, name);
                    editor.apply();

                    // Show user toast for successful save
                    Toast.makeText(MainActivity.this, "Added " + name + " to records successfully!", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Show user Attendance record Screen
        mBtnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AttendanceActivity.class);
                startActivity(i);
            }
        });
    }
}