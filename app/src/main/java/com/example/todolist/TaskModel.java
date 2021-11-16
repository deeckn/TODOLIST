package com.example.todolist;

public class TaskModel implements Comparable<TaskModel> {
    private final int id, status;
    private String text;

    public TaskModel(int id, String text, int status) {
        this.id = id;
        this.text = text;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getStatus() {
        return status;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int compareTo(TaskModel o) {
        return String.valueOf(this.id).compareTo(String.valueOf(o.id));
    }
}
