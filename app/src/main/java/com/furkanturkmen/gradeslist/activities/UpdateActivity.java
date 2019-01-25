package com.furkanturkmen.gradeslist.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.furkanturkmen.gradeslist.R;
import com.furkanturkmen.gradeslist.models.Grade;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    private EditText mGradeViewName;
    private EditText mGradeViewScore;
    private EditText mGradeViewDate;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Init local variables
        mGradeViewName  = findViewById(R.id.textUpdate);
        mGradeViewScore = findViewById(R.id.textUpdateScore);
        mGradeViewDate  = findViewById(R.id.textUpdateDate);


        //Obtain the parameters provided by activties.MainActivity
        final Grade gradeUpdate = getIntent().getParcelableExtra(MainActivity.EXTRA_GRADE);
        mGradeViewName.setText(gradeUpdate.getGradeText());
        mGradeViewScore.setText(gradeUpdate.getGradeScore());
        mGradeViewDate.setText(gradeUpdate.getGradeDate());

        toolbar.setTitle("Updating " + mGradeViewName.getText() + " grade.");

        mGradeViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mGradeViewName.getText().toString();
                String score = mGradeViewScore.getText().toString();
                String date = mGradeViewDate.getText().toString();


                //(gradeUpdate.setGradeText(updatedGradeText)));
                if (!TextUtils.isEmpty(name)) {
                    gradeUpdate.setGradeText(name);
                    gradeUpdate.setGradeScore(score);
                    gradeUpdate.setGradeDate(date);
                    //Prepare the return parameter and return
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(MainActivity.EXTRA_GRADE, gradeUpdate);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Snackbar.make(view, "Enter some data", Snackbar.LENGTH_LONG);
                }
            }
        });
    }
    private void datePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

                calendar.set(year, monthOfYear, dayOfMonth);
                String strDate = format.format(calendar.getTime());

                mGradeViewDate.setText(strDate);
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

}
