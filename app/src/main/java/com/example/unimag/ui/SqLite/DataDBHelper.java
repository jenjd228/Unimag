package com.example.unimag.ui.SqLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class  DataDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "userData";
    public static final String TABLE_CONTACTS = "contacts";
    public static final String KEY_ID = "_id";
    public static final String KEY_SECUREKOD = "email";

    public DataDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Bd has created");
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
        + " integer primary key," + KEY_SECUREKOD + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CONTACTS);
        onCreate(db);
    }
    public String getSecureKod(DataDBHelper dataDbHelper){
        String secureKod = null;
        SQLiteDatabase sqLiteDatabase = dataDbHelper.getWritableDatabase();
                Cursor cursor = sqLiteDatabase.query(DataDBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DataDBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DataDBHelper.KEY_SECUREKOD);
                    cursor.moveToPosition(0);
                    Log.d("mLog", "ID = " + cursor.getString(idIndex) +
                            ", kod = " + cursor.getString(nameIndex)
                    );
                    secureKod = cursor.getString(nameIndex);
                }
                return secureKod;
    }

}
