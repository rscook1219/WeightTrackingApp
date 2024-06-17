package com.zybooks.projecttwo.databases;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;


public class UserDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";

    private static final int VERSION = 1;

    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class UserTable {

        private static final String TABLE = "users";

        private static final String COL_ID = "_id";

        private static final String COL_USERNAME = "username";

        private static final String COL_PWD = "password";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + UserTable.TABLE + " (" + UserTable.COL_ID + " integer primary key autoincrement, " + UserTable.COL_USERNAME + " text, " + UserTable.COL_PWD + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + UserTable.TABLE);
        onCreate(db);
    }
}
