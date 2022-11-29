package com.dam.gpsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dam.gpsapp.db.DbHelper;

import java.util.ArrayList;

public class mostrarListaDb extends AppCompatActivity {

    //Databases
    DbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_lista_db);

        ArrayList<Posicion> posiciones = obtenerPosiciones();

        ArrayList<String> items = new ArrayList<>();

        for (int i = 0; i < posiciones.size(); i++){
            items.add(posiciones.get(i).getFecha() + " | " +
                    posiciones.get(i).getLatitud() + " | " +
                    posiciones.get(i).getLongitud());
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);


        ListView listView = (ListView) findViewById(R.id.lv_datosDb);
        listView.setAdapter(itemsAdapter);




        Log.d("db", obtenerPosiciones().get(0).getFecha());
        Log.d("db", obtenerPosiciones().get(0).getLatitud());
        Log.d("db", obtenerPosiciones().get(0).getLongitud());





    }



    // we have created a new method for reading all the courses.
    public ArrayList<Posicion> obtenerPosiciones() {
        // on below line we are creating a
        // database for reading our database.
        dbHelper = new DbHelper(mostrarListaDb.this);
        db = dbHelper.getWritableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorPosiciones = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_LOCATION, null);

        // on below line we are creating a new array list.
        ArrayList<Posicion> PosicionesArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorPosiciones.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                PosicionesArrayList.add(new Posicion(cursorPosiciones.getString(1),
                        cursorPosiciones.getString(2),
                        cursorPosiciones.getString(3)));
            } while (cursorPosiciones.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorPosiciones.close();
        return PosicionesArrayList;
    }
}