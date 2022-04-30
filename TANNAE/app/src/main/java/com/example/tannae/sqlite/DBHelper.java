package com.example.tannae.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User( USN STRING, " +
                "ID STRING," +
                "PW STRING," +
                "UNAME STRING," +
                "RRN STRING," +
                "SEX BOOLEAN," +
                "PHONE STRING," +
                "EMAIL STRING," +
                "DRIVE BOOLEAN," +
                "POINTS INT," +
                "SCORE FLOAT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
