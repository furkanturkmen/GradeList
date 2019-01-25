package com.furkanturkmen.gradeslist.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.amitshekhar.DebugDB;
import com.furkanturkmen.gradeslist.R;
import com.furkanturkmen.gradeslist.adapters.GradeAdapter;
import com.furkanturkmen.gradeslist.models.Grade;
import com.furkanturkmen.gradeslist.utils.AppDatabase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements GradeAdapter.GradeClickListener {

    private TextView mEditTextName;
    private TextView mEditTextScore;
    private TextView mEditTextDate;
    private List<Grade> mGrades;
    private GradeAdapter mAdapter;
    private RecyclerView mRecyclerView;
    static AppDatabase db;
    public static FirebaseFirestore FireStoreDB;



    //Constants used when calling the update activity
    public static final String EXTRA_GRADE = "Grade";
    public static final int REQUESTCODE = 1234;
    private int mModifyPosition;
    public final static int TASK_GET_ALL_GRADES = 0;
    public final static int TASK_DELETE_GRADE = 1;
    public final static int TASK_UPDATE_GRADE = 2;
    public final static int TASK_INSERT_GRADE = 3;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FireStoreDB = FirebaseFirestore.getInstance();
        mEditTextName = findViewById(R.id.editTextName);
        mEditTextScore = findViewById(R.id.editTextScore);
        mEditTextDate = findViewById(R.id.editTextDate);

        mGrades = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        db = AppDatabase.getInstance(this);
        mEditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog();
            }
        });

        new GradeAsyncTask(TASK_GET_ALL_GRADES).execute();



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = mEditTextName.getText().toString();
                String score = mEditTextScore.getText().toString();
                String date = mEditTextDate.getText().toString();
                Grade newGrade = new Grade(name, score, date);

                //Check if some text has been added
                if (!(TextUtils.isEmpty(name))) {
                    //Add the text to the list (datamodel)
                    mGrades.add(newGrade);
                    new GradeAsyncTask(TASK_INSERT_GRADE).execute(newGrade);


                    //Tell the adapter that the data set has been modified: the screen will be refreshed.
                    updateUI();

                    //Initialize the EditText for the next item
                    mEditTextName.setText("");
                    mEditTextScore.setText("");
                    mEditTextDate.setText("");
                } else {
                    //Show a message to the user if the textfield is empty
                    Snackbar.make(view, "Please enter some text in the textfield",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder
                            target) {
                        return false;
                    }


                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position

                        int position = (viewHolder.getAdapterPosition());
                        new GradeAsyncTask(TASK_DELETE_GRADE).execute(mGrades.get(position));
                        mGrades.remove(position);
                    }
                };


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        DebugDB.getAddressLog();


        updateUI();
//        requestData();
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

                mEditTextDate.setText(strDate);
            }
        }, year, month, day);
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new GradeAdapter(mGrades, this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.swapList(mGrades);
        }
    }

    public void onGradeDbUpdated(List list) {
        mGrades = list;
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void gradeOnClick(int i) {
        Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
        mModifyPosition = i;
        intent.putExtra(EXTRA_GRADE, mGrades.get(i));
        startActivityForResult(intent, REQUESTCODE);
    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUESTCODE) {

            if (resultCode == RESULT_OK) {

                Grade updatedGrade = data.getParcelableExtra(MainActivity.EXTRA_GRADE);

                // New timestamp: timestamp of update
                new GradeAsyncTask(TASK_UPDATE_GRADE).execute(updatedGrade);

            }

        }

    }

    public class GradeAsyncTask extends AsyncTask<Grade, Void, List> {


        private int taskCode;


        public GradeAsyncTask(int taskCode) {

            this.taskCode = taskCode;

        }


        @Override

        protected List doInBackground(Grade... grades) {

            switch (taskCode) {

                case TASK_DELETE_GRADE:
                    db.gradeDao().deleteGrades(grades[0]);
                    break;

                case TASK_UPDATE_GRADE:
                    db.gradeDao().updateGrades(grades[0]);
                    break;

                case TASK_INSERT_GRADE:
                    db.gradeDao().insertGrades(grades[0]);
                    // Add a new document with a generated id.
                    Map<String, Object> data = new HashMap<>();
                    data.put("gradeDate", grades[0].getGradeDate());
                    data.put("gradeScore", grades[0].getGradeScore());
                    data.put("gradeText", grades[0].getGradeText());

                    FireStoreDB.collection("grades")
                            .add(data)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d("MainActivity", "DocumentSnapshot written with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("MainActivity", "Error adding document", e);
                                }
                            });

                    break;

            }


            //To return a new list with the updated data, we get all the data from the database again.
            return db.gradeDao().getAllGrades();

        }


        @Override

        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onGradeDbUpdated(list);
        }
    }
}