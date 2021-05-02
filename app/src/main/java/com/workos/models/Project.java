package com.workos.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable {

    String projectName;
    String projectDescription;
    String projectImagePath;
    Backlog backlog;

    public Project() {
    }

    public Project(String projectImagePath, String projectName, String projectDescription) {
        this.projectImagePath = projectImagePath;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.backlog = initBacklog();
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

    Backlog initBacklog() {
        Backlog backlog = new Backlog();
        backlog.setNumberOfTickets(1);
        ArrayList<Ticket> tickets = new ArrayList<>();
        Ticket ticket = new Ticket();
        ticket.setTitle("Test Ticket");
        tickets.add(ticket);
        backlog.setTickets(tickets);
        backlog.setTitle("Test Backlog");
        return backlog;
    }

}
