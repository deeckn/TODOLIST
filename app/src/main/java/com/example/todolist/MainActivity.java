package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    FloatingActionButton actionButton;
    RecyclerView taskRecyclerView;
    TaskCardAdapter adapter;
    List<TaskModel> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        actionButton = findViewById(R.id.action_button);
        taskRecyclerView = findViewById(R.id.tasks_recyclerview);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasks = db.getAllTasks();
        adapter = new TaskCardAdapter(tasks, this, db, this);
        taskRecyclerView.setAdapter(adapter);
    }
}