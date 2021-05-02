package com.workos.ui.backlog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.workos.R;
import com.workos.models.Project;

public class IssuesFragment extends Fragment {

    private Project project;

    public IssuesFragment() {

    }

    public IssuesFragment(Project project) {

        this.project = project;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_issues, container, false);

        final TextView textView = root.findViewById(R.id.text_backlog);

        if (project != null) {
            textView.setText(project.getProjectName());
        } else {
            textView.setText("This is issues fragment");

        }

        return root;
    }
}