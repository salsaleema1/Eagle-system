package com.workos.ui.projects;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.workos.models.Project;

import java.util.List;

public class ProjectsViewModel extends ViewModel {

    MutableLiveData<List<Project>> projectList; //consider using MutableLiveData

    public ProjectsViewModel() {
        projectList = new MutableLiveData<>();
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList.setValue(projectList);
    }
}