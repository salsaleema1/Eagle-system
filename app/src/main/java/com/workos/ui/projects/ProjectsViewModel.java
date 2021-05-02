package com.workos.ui.projects;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.workos.models.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    List<Project> projectList; //consider using MutableLiveData

    public ProjectsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Projects fragment");
        projectList = new ArrayList<>();
        projectList.add(new Project("https://lh3.googleusercontent.com/a-/AOh14GhesJ4aIbrz_EvydaXY4e0ZIw5teTD73_v4GuZ4", "Test Project12e2","Test project Description"));
        projectList.add(new Project("Test Project 2"));
        projectList.add(new Project("Test Project 3"));
        projectList.add(new Project("Test Project 4"));
    }

    public LiveData<String> getText() {
        return mText;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
}