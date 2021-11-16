package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    FloatingActionButton actionButton;
    RecyclerView taskRecyclerView;
    TaskCardAdapter adapter;
    List<TaskModel> tasks;
    int COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Page Load
        initialize();
        fillRecyclerView();

        // Event Listener
        actionButton.setOnClickListener(openBottomSheet);

        ItemTouchHelper cardBehaviorHelper = new ItemTouchHelper(cardBehavior);
        cardBehaviorHelper.attachToRecyclerView(taskRecyclerView);
    }

    private void initialize() {
        db = new DatabaseHelper(this);
        actionButton = findViewById(R.id.action_button);
        taskRecyclerView = findViewById(R.id.tasks_recyclerview);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fillRecyclerView() {
        tasks = db.getAllTasks();
        for (TaskModel t : tasks) {
            System.out.println(t.getText() + " " + t.getPosition());
        }
        Collections.sort(tasks);
        adapter = new TaskCardAdapter(tasks, this);
        taskRecyclerView.setAdapter(adapter);
        COUNT = tasks.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    private final View.OnClickListener openBottomSheet = v -> {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.new_task,
                        findViewById(R.id.bottomSheetContainer)
                );
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        EditText input = bottomSheetDialog.findViewById(R.id.input_task);
        Button confirmButton = bottomSheetView.findViewById(R.id.confirm_button);

        confirmButton.setOnClickListener(v1 -> {
            try {
                assert input != null;
                String text = input.getText().toString();
                if (!text.equals("")) {
                    db.addNewTask(new TaskModel(0, text, 0, ++COUNT));
                    tasks = db.getAllTasks();
                    adapter.setTaskList(tasks);
                    adapter.notifyDataSetChanged();
                    bottomSheetDialog.cancel();
                }
                else {
                    Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    };

    ItemTouchHelper.SimpleCallback cardBehavior = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.START | ItemTouchHelper.END,
            ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
                              @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(tasks, fromPosition, toPosition);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemMoved(fromPosition, toPosition);

            int i = 0;
            for (TaskModel task : tasks) {
                task.setPosition(i);
                db.updatePosition(task.getId(), i);
                i++;
            }

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            int id = adapter.getId(position);
            if (direction == ItemTouchHelper.LEFT) {
                tasks.remove(position);
                db.deleteTask(id);
                adapter.notifyItemRemoved(position);
            }
            else {
                showUpdateItemDialog(id, position);
            }
            adapter.notifyItemChanged(position);
        }
    };

    @SuppressLint("SetTextI18n")
    private void showUpdateItemDialog(int id, int position) {
        TaskModel currentModel = adapter.getModel(position);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.new_task,
                        findViewById(R.id.bottomSheetContainer)
                );

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        EditText input = bottomSheetDialog.findViewById(R.id.input_task);
        Button confirmButton = bottomSheetView.findViewById(R.id.confirm_button);
        confirmButton.setText("Update");
        assert input != null;
        input.setText(currentModel.getText());

        confirmButton.setOnClickListener(v1 -> {
            try {
                String text = input.getText().toString();
                if (!text.equals("")) {
                    db.updateTask(id, text);
                    currentModel.setText(text);
                    adapter.notifyItemChanged(position);
                    bottomSheetDialog.cancel();
                    Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}