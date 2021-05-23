package com.workos.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Project implements Serializable {

    String projectName;
    String projectDescription;
    String projectImagePath;
    Backlog backlog;
    List<Task> tasks;

    public Project() {
    }

    public Project(String projectImagePath, String projectName, String projectDescription) {
        this.projectImagePath = projectImagePath;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
    }

    public Project(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectImagePath() {
        return projectImagePath;
    }

    public void setProjectImagePath(String projectImagePath) {
        this.projectImagePath = projectImagePath;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public Backlog getBacklog() {
        return backlog;
    }

    public void setBacklog(Backlog backlog) {
        this.backlog = backlog;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectName='" + projectName + '\'' +
                ", projectDescription='" + projectDescription + '\'' +
                ", projectImagePath='" + projectImagePath + '\'' +
                ", backlog=" + backlog +
                ", tasks=" + tasks +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(projectName, project.projectName) &&
                Objects.equals(projectDescription, project.projectDescription) &&
                Objects.equals(projectImagePath, project.projectImagePath) &&
                Objects.equals(backlog, project.backlog) &&
                Objects.equals(tasks, project.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectName, projectDescription, projectImagePath, backlog, tasks);
    }
}
