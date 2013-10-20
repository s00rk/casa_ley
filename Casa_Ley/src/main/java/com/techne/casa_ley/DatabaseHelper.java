package com.techne.casa_ley;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Victor on 19/10/13.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Version
    private static final int DATABASE_VERSION = 1;

    // Base de Datos
    private static final String DATABASE_NAME = "CasaLey";

    // Tablas
    private static final String TABLA_RECORDATORIO = "Recordatorios";

    // Columnas Principales
    private static final String KEY_ID = "id";

    // TABLE_Recordatorio
    private static final String KEY_TITULO = "titulo";
    private static final String KEY_FECHA = "fecha";

    // Table Create Statements
    // Todo table create statement
    private static final String CREAR_TABLA_RECORDATORIO = "CREATE TABLE "
            + TABLA_RECORDATORIO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITULO
            + " TEXT," + KEY_FECHA + " INT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_RECORDATORIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_RECORDATORIO);
        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public long createRecordatorio(Recordatorio rec) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITULO, rec.getTitulo());
        values.put(KEY_FECHA, rec.getFecha());

        // insert row
        long id = db.insert(TABLA_RECORDATORIO, null, values);

        return id;
    }

    public Recordatorio getRecordatorio(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLA_RECORDATORIO + " WHERE "
                + KEY_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Recordatorio td = new Recordatorio();
        td.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setTitulo((c.getString(c.getColumnIndex(KEY_TITULO))));
        td.setFecha(c.getString(c.getColumnIndex(KEY_FECHA)));

        return td;
    }

    public List<Recordatorio> getAllRecordatorios() {
        List<Recordatorio> todos = new ArrayList<Recordatorio>();
        String selectQuery = "SELECT  * FROM " + TABLA_RECORDATORIO;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Recordatorio td = new Recordatorio();
                td.set_id(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setTitulo((c.getString(c.getColumnIndex(KEY_TITULO))));
                td.setFecha(c.getString(c.getColumnIndex(KEY_FECHA)));

                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    public int updateRecordatorio(Recordatorio todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITULO, todo.getTitulo());
        values.put(KEY_FECHA, todo.getFecha());

        return db.update(TABLA_RECORDATORIO, values, KEY_ID + " = ?",
                new String[] { String.valueOf(todo.get_id()) });
    }

    public void deleteToDo(long tado_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_RECORDATORIO, KEY_ID + " = ?",
                new String[] { String.valueOf(tado_id) });
    }

}
