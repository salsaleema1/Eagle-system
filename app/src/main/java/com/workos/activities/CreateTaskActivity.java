package com.workos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.workos.R;
import com.workos.models.Task;
import com.workos.models.TaskStatus;
import com.workos.models.UserModel;

import java.util.Date;

public class CreateTaskActivity extends AppCompatActivity {
    private TextInputLayout taskName, taskDescription, user;
    Button createTaskButton, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        taskName = findViewById(R.id.taskName);
        taskDescription = findViewById(R.id.taskDescription);
        createTaskButton = findViewById(R.id.createTaskButton);
        user = findViewById(R.id.assignedUser);
        cancel = findViewById(R.id.cancelCreateTaskButton);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Create Task");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<TaskStatus> enumAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, TaskStatus.values());
        enumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(enumAdapter);

        spinner.setSelection(0);

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskNameString = taskName.getEditText().getText().toString();
                String taskDescriptionString = taskDescription.getEditText().getText().toString();
                String userString = user.getEditText().getText().toString();
                if (taskNameString.isEmpty()) {
                    taskName.setError("Task title is required!");
                } else {
                    taskName.setError(null);
                    taskName.setErrorEnabled(false);
                }
                if (taskDescriptionString.isEmpty()) {
                    taskDescription.setError("Task description is required!");
                } else {
                    taskDescription.setError(null);
                    taskDescription.setErrorEnabled(false);
                }
                TaskStatus taskStatus = TaskStatus.getByIndex(spinner.getSelectedItemPosition());
                if (!taskDescriptionString.isEmpty() && !taskNameString.isEmpty()) {
                    Intent data = new Intent();
                    Task task = new Task(taskNameString, taskStatus);
                    task.setDescription(taskDescriptionString);
                    if (!userString.isEmpty()) {
                        UserModel user = new UserModel();
                        user.setUsername(userString);
                        task.setAssignedUser(user);
                    }
                    task.setDate(new Date().toString());
                    data.putExtra("task",task);
                    // Activity finished ok, return the data
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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