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

    public TaskCardAdapter(List<TaskModel> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
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

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DatabaseHelper db = new DatabaseHelper(buttonView.getContext());
            if (isChecked) db.updateStatus(taskList.get(position).getId(), 1);
            else db.updateStatus(taskList.get(position).getId(), 0);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<TaskModel> tasks) {
        this.taskList = tasks;
    }

    public int getId(int position) {
        return taskList.get(position).getId();
    }

    public TaskModel getModel(int position) {
        return taskList.get(position);
    }
}
