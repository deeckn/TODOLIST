package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskCardAdapter extends RecyclerView.Adapter<TaskCardHolder> {

    private List<TaskModel> taskList;
    private final Context context;
    DatabaseHelper db;
    MainActivity activity;

    public TaskCardAdapter(List<TaskModel> taskList, Context context, DatabaseHelper db, MainActivity activity) {
        this.taskList = taskList;
        this.context = context;
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TaskCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        return new TaskCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskCardHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.checkBox.setText(taskList.get(position).getText());
        holder.checkBox.setTextColor(ContextCompat.getColor(context, R.color.gun_metal));
        holder.checkBox.setTextSize(16);
        holder.checkBox.setTypeface(ResourcesCompat.getFont(context, R.font.poppins_semibold));
        holder.checkBox.setChecked(taskList.get(position).getStatus() == 1);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<TaskModel> tasks) {
        this.taskList = tasks;
    }
}
