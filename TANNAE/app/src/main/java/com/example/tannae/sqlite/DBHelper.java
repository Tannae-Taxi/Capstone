package com.example.tannae.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context, int version) {
        super(context, "TT.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table User ( _id integer primary key autoincrement," +
                "usn text, " +
                "id text, " +
                "pw text, " +
                "uname text, " +
                "rrn text, " +
                "gender integer, " +
                "phone text, " +
                "email text, " +
                "drive boolean, " +
                "points integer, " +
                "score real )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        onCreate(db);
    }
}
