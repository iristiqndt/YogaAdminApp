package com.tiqndt.yogaadminapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    private TextInputEditText editTextTime, editTextCapacity, editTextPrice, editTextDuration, editTextDescription;
    private Spinner spinnerCourseType, spinnerDayOfWeek;
    private Button buttonUpdate, buttonDelete, buttonManageInstances;
    private DatabaseHelper db;
    private Course currentCourse;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        initViews();
        db = new DatabaseHelper(this);
        courseId = getIntent().getIntExtra("COURSE_ID", -1);

        if (courseId != -1) {
            currentCourse = db.getCourse(courseId);

            if (currentCourse != null) {
                loadCourseData();
                buttonUpdate.setOnClickListener(v -> updateCourse());
                buttonDelete.setOnClickListener(v -> confirmDelete());

                buttonManageInstances.setOnClickListener(v -> {
                    Intent intent = new Intent(CourseDetailActivity.this, InstanceListActivity.class);
                    intent.putExtra("COURSE_ID", currentCourse.getId());
                    intent.putExtra("COURSE_DAY_OF_WEEK", currentCourse.getDay());
                    startActivity(intent);
                });
            } else {
                Toast.makeText(this, "Error: Course data not found.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error: Invalid Course ID.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        editTextTime = findViewById(R.id.editTextTime);
        editTextCapacity = findViewById(R.id.editTextCapacity);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDuration = findViewById(R.id.editTextDuration);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCourseType = findViewById(R.id.spinnerCourseType);
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonManageInstances = findViewById(R.id.buttonManageInstances);
        setupSpinners();
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> courseTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.course_types, android.R.layout.simple_spinner_item);
        courseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourseType.setAdapter(courseTypeAdapter);

        ArrayAdapter<CharSequence> dayOfWeekAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayOfWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayOfWeek.setAdapter(dayOfWeekAdapter);
    }

    private void loadCourseData() {
        editTextTime.setText(currentCourse.getTime());
        editTextCapacity.setText(String.valueOf(currentCourse.getCapacity()));
        editTextPrice.setText(String.valueOf(currentCourse.getPrice()));
        editTextDuration.setText(String.valueOf(currentCourse.getDuration()));
        editTextDescription.setText(currentCourse.getDescription());
        setSpinnerSelection(spinnerCourseType, currentCourse.getType());
        setSpinnerSelection(spinnerDayOfWeek, currentCourse.getDay());
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if (adapter.getItem(position).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(position);
                return;
            }
        }
    }

    private void updateCourse() {
        String type = spinnerCourseType.getSelectedItem().toString();
        String day = spinnerDayOfWeek.getSelectedItem().toString();
        String time = editTextTime.getText().toString().trim();
        String capacityStr = editTextCapacity.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();
        String durationStr = editTextDuration.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        if (time.isEmpty() || capacityStr.isEmpty() || priceStr.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        currentCourse.setType(type);
        currentCourse.setDay(day);
        currentCourse.setTime(time);
        currentCourse.setCapacity(Integer.parseInt(capacityStr));
        currentCourse.setPrice(Double.parseDouble(priceStr));
        currentCourse.setDuration(Integer.parseInt(durationStr));
        currentCourse.setDescription(description);

        db.updateCourse(currentCourse);
        Toast.makeText(this, "Course updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this course and all its instances?")
                .setPositiveButton("Yes", (dialog, which) -> deleteCourse())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteCourse() {
        List<ClassInstance> instances = db.getAllInstancesForCourse(currentCourse.getId());
        for (ClassInstance instance : instances) {
            db.deleteInstance(instance.getId());
        }
        db.deleteCourse(currentCourse);
        Toast.makeText(this, "Course deleted successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}