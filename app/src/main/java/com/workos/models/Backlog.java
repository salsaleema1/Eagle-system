package com.workos.models;

import java.util.List;

public class Backlog {

    String title;
    int numberOfTickets;
    List<Task> tasks;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public List<Task> getTickets() {
        return tasks;
    }

    public void setTickets(List<Task> tasks) {
        this.tasks = tasks;
    }
}
