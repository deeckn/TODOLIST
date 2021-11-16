package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // DATABASE NAME
    public static final String DB_NAME = "todolist.sqlite";

    // TASK_DATA TABLE COLUMNS
    private static final String TASKS_DATA = "TASKS_DATA";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_TEXT = "TEXT";
    private static final String COLUMN_STATUS = "STATUS";
    private static final String COLUMN_POSITION = "POSITION";

    // CONSTRUCTOR
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    // DATABASE CREATION
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement =
                "CREATE TABLE " +
                        TASKS_DATA + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TEXT + " TEXT, " +
                        COLUMN_STATUS + " INTEGER, " +
                        COLUMN_POSITION + " INTEGER)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // Adds a new task to the TASKS_DATA table
    public void addNewTask(TaskModel taskModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TEXT, taskModel.getText());
        cv.put(COLUMN_STATUS, taskModel.getStatus());
        cv.put(COLUMN_POSITION, taskModel.getPosition());
        db.insert(TASKS_DATA, null, cv);
        db.close();
    }

    // Retrieves all the tasks from the TASKS_DATA table
    public List<TaskModel> getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<TaskModel> allTasks = new ArrayList<>();
        String query = "SELECT * FROM " + TASKS_DATA;

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String text = cursor.getString(1);
                    int status = cursor.getInt(2);
                    int position = cursor.getInt(3);
                    System.out.println("POSITION FROM DATABASE = " + position);
                    TaskModel task = new TaskModel(id, text, status, position);
                    allTasks.add(task);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        catch (Exception ex) {
            System.out.println("Database Error");
        }
        return allTasks;
    }

    // Updates the completion status of a task
    public void updateStatus(int id, int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATUS, status);
        db.update(TASKS_DATA, cv, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
    }

    // Updates the text of the task
    public void updateTask(int id, String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TEXT, text);
        db.update(TASKS_DATA, cv, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
    }

    // Updates the position of the task
    public void updatePosition(int id, int toPosition) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_POSITION, toPosition);
        db.update(TASKS_DATA, cv, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
    }

    // Deletes a task with an ID
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TASKS_DATA, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
        db.close();
    }

    // Clears the TASKS_DATA table
    private void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TASKS_DATA, null, null);
        db.close();
    }
}
