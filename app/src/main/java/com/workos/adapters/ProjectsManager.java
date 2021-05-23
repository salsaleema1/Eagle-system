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
import com.workos.models.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectsManager {

    static FirebaseDatabase rootNode;
    static DatabaseReference reference;
    static List<Project> projects;
    static Context context;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("projects");
        projects = new ArrayList<>();
    }

    public static void saveProjects(List<Project> projects) {
//        for (Project project : projects) {
//            reference.child(project.getProjectName()).setValue(project);
//        }
        projects = projects;
        reference.setValue(projects);
    }

    public static void removeProject(Project project) {
        projects.remove(project);
        reference.setValue(projects);
    }

    public interface MyCallback {
        void onCallback(List<Project> projects);
    }

    public static void readData(MyCallback myCallback) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Project>> t = new GenericTypeIndicator<List<Project>>() {
                };
                List<Project> projects = dataSnapshot.getValue(t);
                if (projects != null) {
                    ProjectsManager.projects = projects;
                    myCallback.onCallback(projects);
                }
                Log.w("ProjectsManager", "Got the values!" + projects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public static void addProject(Project project, MyCallback myCallback) {
        Query checkProject = reference.orderByChild("projectName").equalTo(project.getProjectName());
//        reference.child()
        checkProject.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(context, "Project " + project.getProjectName() + " Already exist!", Toast.LENGTH_LONG).show();
                } else {
                    if (!projects.contains(project)) {
                        projects.add(project);
                    }
                    reference.setValue(projects);
                    myCallback.onCallback(null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ProjectsManager.context = context;
    }
}
