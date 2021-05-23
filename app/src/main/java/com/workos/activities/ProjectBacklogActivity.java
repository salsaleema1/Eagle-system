package com.workos.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.workos.R;
import com.workos.models.Project;

import java.io.Serializable;

public class ProjectBacklogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_backlog);
        String project = getIntent().getStringExtra("Project Name");

        TextView textView = findViewById(R.id.projectTitleTextView);
        textView.setText(project);
    }
}