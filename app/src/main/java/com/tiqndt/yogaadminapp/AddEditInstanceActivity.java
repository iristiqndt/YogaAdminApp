package com.tiqndt.yogaadminapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditInstanceActivity extends AppCompatActivity {

    private static final String TAG = "YOGA_APP_DEBUG";

    private EditText editTextDate, editTextTeacher, editTextComments;
    private Button buttonSaveInstance, buttonDeleteInstance;
    private TextView textViewTitle;
    private Calendar myCalendar;
    private DatabaseHelper db;

    private boolean isEditMode = false;
    private int courseId;
    private int instanceId;
    private String courseDayOfWeek;
    private ClassInstance currentInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_instance);

        initViews();
        db = new DatabaseHelper(this);
        myCalendar = Calendar.getInstance();

        instanceId = getIntent().getIntExtra("INSTANCE_ID", -1);
        courseId = getIntent().getIntExtra("COURSE_ID", -1);
        courseDayOfWeek = getIntent().getStringExtra("COURSE_DAY_OF_WEEK");

        Log.d(TAG, "AddEditInstanceActivity - onCreate: Received instanceId = " + instanceId);
        Log.d(TAG, "AddEditInstanceActivity - onCreate: Received courseId = " + courseId);
        Log.d(TAG, "AddEditInstanceActivity - onCreate: Received courseDayOfWeek = '" + courseDayOfWeek + "'");

        if (instanceId != -1) {
            isEditMode = true;
            Log.d(TAG, "AddEditInstanceActivity - Entering EDIT mode.");
            textViewTitle.setText("Edit Class Instance");
            buttonSaveInstance.setText("Update");
            buttonDeleteInstance.setVisibility(View.VISIBLE);
            loadInstanceData();
        } else {
            isEditMode = false;
            Log.d(TAG, "AddEditInstanceActivity - Entering ADD mode.");
            textViewTitle.setText("Add New Class Instance");
            buttonSaveInstance.setText("Save");
            buttonDeleteInstance.setVisibility(View.GONE);
        }

        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        };
        editTextDate.setOnClickListener(v -> new DatePickerDialog(AddEditInstanceActivity.this, dateSetListener,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH))
                .show());

        buttonSaveInstance.setOnClickListener(v -> saveInstance());
        buttonDeleteInstance.setOnClickListener(v -> confirmDelete());
    }

    private void initViews() {
        editTextDate = findViewById(R.id.editTextDate);
        editTextTeacher = findViewById(R.id.editTextTeacher);
        editTextComments = findViewById(R.id.editTextComments);
        buttonSaveInstance = findViewById(R.id.buttonSaveInstance);
        buttonDeleteInstance = findViewById(R.id.buttonDeleteInstance);
        textViewTitle = findViewById(R.id.textViewTitle);
    }

    private void loadInstanceData() {
        currentInstance = db.getInstance(instanceId);
        if (currentInstance != null) {
            Log.d(TAG, "AddEditInstanceActivity - Loading data for instance ID: " + instanceId);
            editTextTeacher.setText(currentInstance.getTeacherName());
            editTextComments.setText(currentInstance.getComments());
            editTextDate.setText(currentInstance.getDate());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
            try {
                myCalendar.setTime(sdf.parse(currentInstance.getDate()));
            } catch (ParseException e) {
                Log.e(TAG, "Error parsing date when loading instance data.", e);
            }
        } else {
            Log.e(TAG, "AddEditInstanceActivity - Error: Could not find instance with ID: " + instanceId);
            Toast.makeText(this, "Error loading instance data.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateDateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        editTextDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void saveInstance() {
        Log.d(TAG, "saveInstance button clicked.");
        String date = editTextDate.getText().toString().trim();
        String teacher = editTextTeacher.getText().toString().trim();
        String comments = editTextComments.getText().toString().trim();

        if (date.isEmpty() || teacher.isEmpty()) {
            Toast.makeText(this, "Date and Teacher name are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (courseDayOfWeek == null || courseDayOfWeek.isEmpty()) {
            Toast.makeText(this, "Error: Course day of week is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        String dayOfWeekFromDate = dayFormat.format(myCalendar.getTime());

        if (!dayOfWeekFromDate.equalsIgnoreCase(courseDayOfWeek)) {
            String errorMessage = "The selected date is a " + dayOfWeekFromDate + ", but the course runs on "
                    + courseDayOfWeek + ".";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            return;
        }

        if (isEditMode) {
            Log.d(TAG, "Attempting to UPDATE instance ID: " + currentInstance.getId());
            currentInstance.setDate(date);
            currentInstance.setTeacherName(teacher);
            currentInstance.setComments(comments);
            db.updateInstance(currentInstance);
            Toast.makeText(this, "Instance updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Attempting to ADD new instance for course ID: " + courseId);
            ClassInstance instance = new ClassInstance(date, teacher, comments, courseId);
            db.addInstance(instance);
            Toast.makeText(this, "Instance saved successfully", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this class instance?")
                .setPositiveButton("Yes", (dialog, which) -> deleteInstance())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteInstance() {
        if (currentInstance != null) {
            db.deleteInstance(currentInstance.getId());
            Toast.makeText(this, "Instance deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}