package com.example.tannae.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, int version) {
        super(context, "TTdb", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "usn STRING," +
                "id STRING," +
                "pw STRING," +
                "uname STRING," +
                "rrn STRING," +
                "sex BOOLEAN," +
                "phone STRING," +
                "email STRING," +
                "drive BOOLEAN," +
                "points INTEGER," +
                "score FLOAT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }
}
