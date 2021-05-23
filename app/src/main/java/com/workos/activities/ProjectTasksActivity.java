package com.workos.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.workos.R;
import com.workos.adapters.TasksManager;
import com.workos.adapters.TasksRecyclerAdapter;
import com.workos.models.Task;
import com.workos.models.TaskStatus;
import com.workos.models.UserModel;

import java.io.Serializable;
import java.util.List;

import static com.workos.ui.projects.ProjectsFragment.ic_menu_add;

public class ProjectTasksActivity extends AppCompatActivity implements TasksRecyclerAdapter.ItemClickListener {

    protected RecyclerView mRecyclerView;
    protected TasksRecyclerAdapter tasksRecyclerAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    public static final String TAG = "ProjectTasksActivity";
    private static final int CREATE_TASK_ID = 2;
    private static final int DELETE_TASK_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_tasks);
        String project = getIntent().getStringExtra("Project Name");
        mLayoutManager = new LinearLayoutManager(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(project);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mRecyclerView = findViewById(R.id.tasksList);
        tasksRecyclerAdapter = new TasksRecyclerAdapter(this);

        mRecyclerView.setAdapter(tasksRecyclerAdapter);
        tasksRecyclerAdapter.setProjectName(project);
        TasksManager.readData(project, new TasksManager.MyCallback() {
            @Override
            public void onCallback(List<Task> callbackTasks) {
                tasksRecyclerAdapter.setTasksList(callbackTasks);
                tasksRecyclerAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    //
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(Menu.NONE, CREATE_TASK_ID, Menu.NONE, "AddTask").setIcon(ic_menu_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(Menu.NONE, DELETE_TASK_ID, Menu.NONE, "Delete Task").setIcon(ic_menu_add);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case CREATE_TASK_ID:
                Intent intent = new Intent(this, CreateTaskActivity.class);
                startActivityForResult(intent, 0);
                return true;
//            case DELETE_TASK_ID:
//                Intent intent = new Intent(this, CreateTaskActivity.class);
//                startActivityForResult(intent, 0);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            if (data.hasExtra("task")) {

                Task task = (Task) data.getSerializableExtra("task");
                tasksRecyclerAdapter.addItem(task);
            }
        }
    }

    @Override
    public void onItemClick(Task task) {

        Intent intent = new Intent(this, TaskViewActivity.class);
        intent.putExtra("task", task);
        startActivityForResult(intent, 0);
    }

}