package com.example.attendanceapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;

public class StudentAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] studentNames;
    public HashMap<String, String> checked = new HashMap<>();

    public StudentAdapter(Activity context, String[] studentNames) {
        super(context, R.layout.student_list_item, studentNames);
        this.context = context;
        this.studentNames = studentNames;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.student_list_item, null, true);

        TextView txtStudentName = (TextView) rowView.findViewById(R.id.txt_student_name);
        txtStudentName.setText(studentNames[position]);

        return rowView;
    }

    public void setCheckedItem(int item) {
        if (checked.containsKey(studentNames[item])) {
            checked.remove(studentNames[item]);
        } else {
            checked.put(studentNames[item], studentNames[item]);
        }
    }

    public HashMap<String, String> getCheckedItems() {
        return checked;
    }
}
