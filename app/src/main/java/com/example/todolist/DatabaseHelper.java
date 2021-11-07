package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "todolist.sqlite";
    public static final String TASKS_DATA = "TASKS_DATA";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_TEXT = "TEXT";
    public static final String COLUMN_STATUS = "STATUS";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement =
                "CREATE TABLE " +
                        TASKS_DATA + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TEXT + " TEXT, " +
                        COLUMN_STATUS + " INTEGER)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void addNewTask(TaskModel taskModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, taskModel.getId());
        cv.put(COLUMN_TEXT, taskModel.getText());
        cv.put(COLUMN_STATUS, taskModel.getStatus());
        db.insert(TASKS_DATA, null, cv);
        db.close();
    }

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
                    TaskModel task = new TaskModel(id, text, status);
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

    public void updateStatus(int id, int status) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATUS, status);
        db.update(TASKS_DATA, cv, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String text) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TEXT, text);
        db.update(TASKS_DATA, cv, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TASKS_DATA, COLUMN_ID + "=?", new String[] {String.valueOf(id)});
    }
}
