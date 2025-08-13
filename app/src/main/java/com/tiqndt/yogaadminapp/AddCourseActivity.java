package com.tiqndt.yogaadminapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;

public class AddCourseActivity extends AppCompatActivity {

    private Spinner spinnerCourseType, spinnerDayOfWeek;
    private TextInputEditText editTextTime, editTextCapacity, editTextPrice, editTextDuration, editTextDescription;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        spinnerCourseType = findViewById(R.id.spinnerCourseType);
        spinnerDayOfWeek = findViewById(R.id.spinnerDayOfWeek);
        editTextTime = findViewById(R.id.editTextTime);
        editTextCapacity = findViewById(R.id.editTextCapacity);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDuration = findViewById(R.id.editTextDuration);
        editTextDescription = findViewById(R.id.editTextDescription);
        buttonSave = findViewById(R.id.buttonSave);

        ArrayAdapter<CharSequence> courseTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.course_types, android.R.layout.simple_spinner_item);
        courseTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourseType.setAdapter(courseTypeAdapter);

        ArrayAdapter<CharSequence> dayOfWeekAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayOfWeekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDayOfWeek.setAdapter(dayOfWeekAdapter);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = spinnerCourseType.getSelectedItem().toString();
                String day = spinnerDayOfWeek.getSelectedItem().toString();
                String time = editTextTime.getText().toString().trim();
                String capacityStr = editTextCapacity.getText().toString().trim();
                String priceStr = editTextPrice.getText().toString().trim();
                String durationStr = editTextDuration.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();

                if (time.isEmpty() || capacityStr.isEmpty() || priceStr.isEmpty() || durationStr.isEmpty()) {
                    Toast.makeText(AddCourseActivity.this, "Please fill all required fields", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                int capacity = Integer.parseInt(capacityStr);
                double price = Double.parseDouble(priceStr);
                int duration = Integer.parseInt(durationStr);

                Course newCourse = new Course(type, day, time, capacity, price, duration, description);

                DatabaseHelper db = new DatabaseHelper(AddCourseActivity.this);
                db.addCourse(newCourse);

                Toast.makeText(AddCourseActivity.this, "Course saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}