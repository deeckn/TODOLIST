package com.example.todolist;

import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskCardHolder extends RecyclerView.ViewHolder {

    CheckBox checkBox;

    public TaskCardHolder(@NonNull View itemView) {
        super(itemView);
        this.checkBox = itemView.findViewById(R.id.task_checkbox);
    }
}
