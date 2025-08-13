package com.tiqndt.yogaadminapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_Debug";

    private RecyclerView recyclerViewCourses;
    private FloatingActionButton fabAddCourse;
    private CourseAdapter courseAdapter;
    private List<Course> courseList;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        courseList = new ArrayList<>();
        recyclerViewCourses = findViewById(R.id.recyclerViewCourses);
        fabAddCourse = findViewById(R.id.fabAddCourse);
        courseAdapter = new CourseAdapter(courseList, this);
        recyclerViewCourses.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewCourses.setAdapter(courseAdapter);
        fabAddCourse.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_upload) {
            Log.d(TAG, "Upload menu item clicked.");
            Toast.makeText(this, "Starting upload...", Toast.LENGTH_SHORT).show();
            uploadDataToFirestore();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadDataToFirestore() {
        Log.d(TAG, "uploadDataToFirestore method called.");

        if (!isNetworkAvailable()) {
            Log.e(TAG, "Upload failed: No Internet Connection.");
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Course> coursesToUpload = db.getAllCourses();
        if (coursesToUpload.isEmpty()) {
            Log.w(TAG, "Upload stopped: No data to upload.");
            Toast.makeText(this, "No data to upload.", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Found " + coursesToUpload.size() + " courses to upload.");
        FirebaseFirestore firestoreDb = FirebaseFirestore.getInstance();

        int totalCourses = coursesToUpload.size();
        AtomicInteger successfulUploads = new AtomicInteger(0);
        AtomicInteger failedUploads = new AtomicInteger(0);

        for (Course course : coursesToUpload) {
            final Course currentCourse = course;
            Map<String, Object> courseMap = new HashMap<>();
            courseMap.put("type", currentCourse.getType());
            courseMap.put("day", currentCourse.getDay());
            courseMap.put("time", currentCourse.getTime());
            courseMap.put("capacity", currentCourse.getCapacity());
            courseMap.put("price", currentCourse.getPrice());
            courseMap.put("duration", currentCourse.getDuration());
            courseMap.put("description", currentCourse.getDescription());

            firestoreDb.collection("courses").document(String.valueOf(currentCourse.getId()))
                    .set(courseMap)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Successfully uploaded course ID: " + currentCourse.getId());
                        uploadInstancesForCourse(firestoreDb, currentCourse.getId());
                        int currentSuccesses = successfulUploads.incrementAndGet();
                        if (currentSuccesses + failedUploads.get() == totalCourses) {
                            Toast.makeText(MainActivity.this,
                                    "Upload complete! " + currentSuccesses + " courses uploaded.", Toast.LENGTH_LONG)
                                    .show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to upload course ID: " + currentCourse.getId(), e);
                        int currentFails = failedUploads.incrementAndGet();
                        if (successfulUploads.get() + currentFails == totalCourses) {
                            Toast.makeText(MainActivity.this,
                                    "Upload finished with " + currentFails + " errors. Check Logcat for details.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void uploadInstancesForCourse(FirebaseFirestore firestoreDb, int courseId) {
        List<ClassInstance> instancesToUpload = db.getAllInstancesForCourse(courseId);
        Log.d(TAG, "Uploading " + instancesToUpload.size() + " instances for course ID: " + courseId);
        if (instancesToUpload.isEmpty()) {
            return;
        }

        for (ClassInstance instance : instancesToUpload) {
            Map<String, Object> instanceMap = new HashMap<>();
            instanceMap.put("date", instance.getDate());
            instanceMap.put("teacherName", instance.getTeacherName());
            instanceMap.put("comments", instance.getComments());
            instanceMap.put("courseId", instance.getCourseId());

            firestoreDb.collection("courses").document(String.valueOf(courseId))
                    .collection("instances").document(String.valueOf(instance.getId()))
                    .set(instanceMap)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Successfully uploaded instance ID: " + instance.getId()))
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to upload instance ID: " + instance.getId(), e));
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllCourses();
    }

    private void loadAllCourses() {
        courseList.clear();
        courseList.addAll(db.getAllCourses());
        courseAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCourses(newText);
                return true;
            }
        });
        searchView.setOnCloseListener(() -> {
            loadAllCourses();
            return false;
        });
        return true;
    }

    private void filterCourses(String text) {
        List<Course> fullList = db.getAllCourses();
        List<Course> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(fullList);
        } else {
            for (Course course : fullList) {
                if (course.getType().toLowerCase().contains(text.toLowerCase()) ||
                        course.getDay().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(course);
                }
            }
        }
        courseList.clear();
        courseList.addAll(filteredList);
        courseAdapter.notifyDataSetChanged();
    }
}