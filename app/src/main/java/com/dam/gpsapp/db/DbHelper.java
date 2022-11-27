package com.dam.gpsapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "location.db";
    public static final String TABLE_LOCATION = "t_location";


    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_LOCATION + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "fecha TEXT NOT NULL," +
                "latitud NUMBER NOT NULL," +
                "longitud NUMBER NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_LOCATION);
        onCreate(sqLiteDatabase);

    }
}
