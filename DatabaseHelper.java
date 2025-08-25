package com.example.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "InventoryApp.db";
    public static final int DATABASE_VERSION = 3;

    // table for the users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID= "id";
    public static final String COLUMN_USERNAME="username";
    public static final String COLUMN_PASSWORD= "password";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating the user table.
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        String createInventoryTable = "CREATE TABLE inventory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "item_name TEXT NOT NULL, " +
                "quantity INTEGER NOT NULL, " +
                "description TEXT" +
                ");";
        db.execSQL(createInventoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop and recreate table if the database is updated
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS inventory");
        onCreate(db);
    }
    //Create account
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != 1;
    }
    // Check login
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
    // for insert inventory items
    public boolean insertInventoryItem(String itemName, int quantity, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("item_name", itemName);
        values.put("quantity", quantity);
        values.put("description", description);

        long result = db.insert("inventory", null, values);
        return result != -1; //returns true if successful.
    }
    //for get all inventory items
    public Cursor getAllInventoryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM inventory", null);
    }
    // for update inventory item
    public boolean updateInventoryItem(int id, String itemName, int quantity, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("item_name", itemName);
        values.put("quantity", quantity);
        values.put("description", description);

        int result = db.update("inventory", values, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    //for delete inventory item
    public boolean deleteInventoryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("inventory", "id=?", new String[]{String.valueOf(id)});
        return result > 0; //return true if at least 1 row was deleted.
    }
}
