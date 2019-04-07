package com.example.maria.terraport;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "terraport_db";
    private static final int DB_VERSION = 1;
    DBHelper(Context context){
        super(context, DB_NAME,null,DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CONT_LIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, URL TEXT, DESCRIPTION TEXT, DELETED INTEGER);");
        ContentValues contValues = new ContentValues();
        contValues.put("URL", "http://192.168.0.130/json");
        contValues.put("DESCRIPTION", "Lumos");
        contValues.put("DELETED", 0);
        db.insert("CONT_LIST", null,contValues);
        contValues = new ContentValues();
        contValues.put("URL", "http://192.168.0.120/json");
        contValues.put("DESCRIPTION", "ADroid");
        contValues.put("DELETED", 0);
        db.insert("CONT_LIST", null,contValues);
        contValues = null;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CONT_LIST ");
        onCreate(db);
    }
}
