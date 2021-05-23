package com.workos.ui.projects;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workos.R;
import com.workos.activities.CreateProjectActivity;
import com.workos.activities.ProjectTasksActivity;
import com.workos.adapters.ProjectsManager;
import com.workos.adapters.ProjectsRecyclerAdapter;
import com.workos.models.Project;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProjectsFragment extends Fragment implements ProjectsRecyclerAdapter.ItemClickListener {

    protected RecyclerView mRecyclerView;
    protected ProjectsRecyclerAdapter projectsRecyclerAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private static final int MENU_ITEM_ITEM1 = 1;
    public static final int ic_menu_add = 17301555;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_projects, container, false);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView = root.findViewById(R.id.projectsList);
        projectsRecyclerAdapter = new ProjectsRecyclerAdapter(this);

        mRecyclerView.setAdapter(projectsRecyclerAdapter);
        setHasOptionsMenu(true);

        ProjectsManager.readData(new ProjectsManager.MyCallback() {
            @Override
            public void onCallback(List<Project> callbackProjects) {
                projectsRecyclerAdapter.setProjectList(callbackProjects);
                projectsRecyclerAdapter.notifyDataSetChanged();
            }
        });

        mRecyclerView.setLayoutManager(mLayoutManager);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, MENU_ITEM_ITEM1, Menu.NONE, "AddProject").setIcon(ic_menu_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemClick(Project project) {

        Intent intent = new Intent(getContext(), ProjectTasksActivity.class);
        intent.putExtra("Project Name", project.getProjectName());
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_ITEM1:
                Intent intent = new Intent(getContext(), CreateProjectActivity.class);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            if (data.hasExtra("projectName")) {
                String projectName = data.getStringExtra("projectName");
                projectsRecyclerAdapter.addItem(new Project(projectName));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w("ProjectsFragment", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w("ProjectsFragment", "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("ProjectsFragment", "onDestroy");

    }


}