package com.tiqndt.yogaadminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class InstanceListActivity extends AppCompatActivity {

    private static final String TAG = "YOGA_APP_DEBUG";

    private RecyclerView recyclerViewInstances;
    private FloatingActionButton fabAddInstance;
    private InstanceAdapter instanceAdapter;
    private List<ClassInstance> instanceList; // Danh sách này sẽ được cập nhật liên tục
    private DatabaseHelper db;
    private int courseId;
    private String courseDayOfWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance_list);

        courseId = getIntent().getIntExtra("COURSE_ID", -1);
        courseDayOfWeek = getIntent().getStringExtra("COURSE_DAY_OF_WEEK");

        db = new DatabaseHelper(this);
        instanceList = new ArrayList<>();
        recyclerViewInstances = findViewById(R.id.recyclerViewInstances);
        fabAddInstance = findViewById(R.id.fabAddInstance);

        instanceAdapter = new InstanceAdapter(instanceList, this, courseDayOfWeek);
        recyclerViewInstances.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewInstances.setAdapter(instanceAdapter);

        fabAddInstance.setOnClickListener(view -> {
            Intent intent = new Intent(InstanceListActivity.this, AddEditInstanceActivity.class);
            intent.putExtra("COURSE_ID", courseId);
            intent.putExtra("COURSE_DAY_OF_WEEK", courseDayOfWeek);
            startActivity(intent);
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInstances();
    }

    private void loadInstances() {
        if (courseId != -1) {
            setTitle("Instances for " + courseDayOfWeek);
            instanceList.clear();
            instanceList.addAll(db.getAllInstancesForCourse(courseId));
            instanceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setQueryHint("Search by Teacher or Date...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterInstances(newText);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            loadInstances();
            return false;
        });

        return true;
    }

    private void filterInstances(String text) {
        List<ClassInstance> filteredList;
        if (text.isEmpty()) {
            filteredList = db.getAllInstancesForCourse(courseId);
        } else {
            filteredList = db.searchInstancesForCourse(text, courseId);
        }

        instanceList.clear();
        instanceList.addAll(filteredList);
        instanceAdapter.notifyDataSetChanged();
    }
}