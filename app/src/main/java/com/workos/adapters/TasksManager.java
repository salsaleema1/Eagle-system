package com.workos.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.workos.models.Task;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksManager {

    static FirebaseDatabase rootNode;
    static DatabaseReference reference;
    static Map<String, List<Task>> projectTasksMap;
    static Context context;

    static {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("tasks");
        projectTasksMap = new HashMap<>();
    }

    public interface MyCallback {
        void onCallback(List<Task> tasks);
    }

    public static void readData(String projectName, MyCallback myCallback) {

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Task>> t = new GenericTypeIndicator<List<Task>>() {
                };
                List<Task> tasks = dataSnapshot.child(projectName).getValue(t);
                if (tasks != null) {
                    projectTasksMap.put(projectName, tasks);
                    myCallback.onCallback(tasks);
                }
                Log.w("TasksManager", "Got the values!" + tasks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public static void addTask(String projectName, Task task, MyCallback myCallback) {
        Query checkProject = reference.child(projectName).orderByChild("title").equalTo(task.getName());
        checkProject.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(context, "Project " + task.getName() + " Already exist!", Toast.LENGTH_LONG).show();
                } else {
                    if (projectTasksMap.containsKey(projectName)) {
                        if (!projectTasksMap.get(projectName).contains(task)) {
                            projectTasksMap.get(projectName).add(task);
                        }
                    } else {
                        projectTasksMap.put(projectName, Collections.singletonList(task));
                    }
                    reference.setValue(projectTasksMap);
                    myCallback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public static void removeTask(String projectName, Task task) {
        projectTasksMap.get(projectName).remove(task);
        reference.setValue(projectTasksMap);
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        TasksManager.context = context;
    }
}
