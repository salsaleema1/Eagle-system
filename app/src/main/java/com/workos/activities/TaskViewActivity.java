package com.workos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.workos.R;
import com.workos.models.Task;
import com.workos.models.TaskStatus;

public class TaskViewActivity extends AppCompatActivity {

    private TextView taskCreateDate, taskDescription, assignedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);
        taskDescription = findViewById(R.id.taskDescription);
        taskCreateDate = findViewById(R.id.createdDate);
        assignedUser = findViewById(R.id.assignedUser);

        Task task = (Task) getIntent().getSerializableExtra("task");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(task.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        taskDescription.setText(task.getDescription());
        taskCreateDate.setText(task.getDate());
        assignedUser.setText(task.getAssignedUser().getUsername());

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<TaskStatus> enumAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TaskStatus.values());
        enumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(enumAdapter);

        spinner.setSelection(task.getStatus().getIndex());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}