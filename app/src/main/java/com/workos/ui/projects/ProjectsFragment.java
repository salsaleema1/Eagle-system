package com.workos.ui.projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workos.R;
import com.workos.activities.ProjectBacklogActivity;
import com.workos.activities.SignupActivity;
import com.workos.adapters.RecyclerAdapter;
import com.workos.models.Project;
import com.workos.ui.backlog.IssuesFragment;

public class ProjectsFragment extends Fragment implements RecyclerAdapter.ItemClickListener {

    private ProjectsViewModel projectsViewModel;
    protected RecyclerView mRecyclerView;
    protected RecyclerAdapter recyclerAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        projectsViewModel =
                new ViewModelProvider(this).get(ProjectsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_projects, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView = root.findViewById(R.id.projectsList);
        recyclerAdapter = new RecyclerAdapter(projectsViewModel.getProjectList(), this);

        mRecyclerView.setAdapter(recyclerAdapter);


        mRecyclerView.setLayoutManager(mLayoutManager);
        return root;
    }

    @Override
    public void onItemClick(Project project) {

        Intent intent = new Intent(getContext(), ProjectBacklogActivity.class);
        intent.putExtra("Project Name",project.getProjectName());
        startActivityForResult(intent, 0);
    }
}