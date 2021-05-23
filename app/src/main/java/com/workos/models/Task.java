package com.workos.models;

import java.io.Serializable;
import java.util.Objects;

public class Task implements Serializable {

    String name;
    String description;
    String date;
    TaskStatus status;
    UserModel assignedUser;

    public Task() {
    }

    public Task(String name, TaskStatus status) {
        this.name = name;
        this.status = status;
    }
    public Task(String name, String taskDescription, TaskStatus status) {
        this.name = name;
        this.description = taskDescription;
        this.status = status;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UserModel getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(UserModel assignedUser) {
        this.assignedUser = assignedUser;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", assignedUser=" + assignedUser +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) &&
                Objects.equals(date, task.date) &&
                Objects.equals(status, task.status) &&
                Objects.equals(assignedUser, task.assignedUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, date, status, assignedUser);
    }
}
