package com.workos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.workos.R;

public class CreateProjectActivity extends AppCompatActivity {

    private TextInputLayout projectName;
    Button createProjectButton, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        projectName = findViewById(R.id.projectName);
        createProjectButton = findViewById(R.id.createProjectButton);
        cancel = findViewById(R.id.cancelCreateProjectButton);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Create Project");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        createProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projectNameString = projectName.getEditText().getText().toString();
                if (projectNameString.isEmpty()) {
                    projectName.setError("Project title is required!");
                } else {
                    projectName.setError(null);
                    projectName.setErrorEnabled(false);
                    Intent data = new Intent();
                    data.putExtra("projectName", projectNameString);
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