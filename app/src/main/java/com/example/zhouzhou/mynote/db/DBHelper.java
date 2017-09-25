package com.example.zhouzhou.mynote.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhouzhou on 2017/9/20.
 */

public class DBHelper extends SQLiteOpenHelper{
    public final static String DB_NAME = "notes";
    public final static int DB_VERSON = 1;
    public DBHelper(Context context) {
        super(context, DB_NAME+".db", null, DB_VERSON);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_NAME + "(_id integer primary key autoincrement" +
                ",title text, content text,create_time text, is_sync text default 'false' not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists" + DB_NAME);
        onCreate(db);
    }
}
