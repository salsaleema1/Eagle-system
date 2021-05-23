package com.workos.models;

public enum TaskStatus {

    Backlog(0),
    InProgress(1),
    Pending(2),
    Testing(3),
    Done(4);

    private int index;

    TaskStatus(int index) {

        this.index = index;
    }

    public int getIndex() {
        return index;
    }
    public static TaskStatus getByIndex(int index){
        for (TaskStatus taskStatus : values()) {
            if (taskStatus.index == index){
                return taskStatus;
            }
        }
        return null;
    }
}
