package com.example.todolist;

public class TaskModel implements Comparable<TaskModel> {
    private final int id, status;
    private int position;
    private String text;

    public TaskModel(int id, String text, int status, int position) {
        this.id = id;
        this.text = text;
        this.status = status;
        this.position = position;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int compareTo(TaskModel o) {
        return Integer.compare(this.position, o.position);
    }
}
