package com.techne.casa_ley;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
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
    private static final String DATABASE_NAME = "Casa_Ley1";

    // Tablas
    private static final String TABLA_RECORDATORIO = "Recordatorios";
    private static final String TABLA_PRODUCTO = "Productos";
    private static final String TABLA_COMPRA = "Compras";
    private static final String TABLA_CONFIG = "Config";

    // Columnas Principales
    private static final String KEY_ID = "_id";

    // Tabla_Recordatorio
    private static final String KEY_TITULO = "titulo";
    private static final String KEY_FECHA = "fecha";

    // Tabla Producto
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_DESC = "descripcion";
    private static final String KEY_COMPRAR = "comprar";

    // Tabla Config
    private static final String KEY_LOCAL = "NotiLocal";
    private static final String KEY_PUSH = "NotiPush";
    private static final String KEY_TIENDAS = "Tiendas";

    // Table Create Statements
    private static final String CREAR_TABLA_RECORDATORIO = "CREATE TABLE IF NOT EXISTS "
            + TABLA_RECORDATORIO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITULO
            + " TEXT," + KEY_FECHA + " TEXT)";
    private static final String CREAR_TABLA_PRODUCTO = "CREATE TABLE IF NOT EXISTS "
            + TABLA_PRODUCTO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOMBRE
            + " TEXT," + KEY_DESC + " TEXT)";
    private static final String CREAR_TABLA_COMPRAS = "CREATE TABLE IF NOT EXISTS "
            + TABLA_COMPRA + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOMBRE
            + " TEXT)";
    private static final String CREAR_TABLA_CONFIG = "CREATE TABLE IF NOT EXISTS "
            + TABLA_CONFIG + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LOCAL + " INTEGER, "
            + KEY_PUSH + " INTEGER, " + KEY_TIENDAS + " INTEGER)";

    private static final String metadata = "CREATE TABLE IF NOT EXISTS \"android_metadata\" (\"locale\" TEXT DEFAULT 'en_US')";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(metadata);
        db.execSQL(CREAR_TABLA_RECORDATORIO);
        db.execSQL(CREAR_TABLA_PRODUCTO);
        db.execSQL(CREAR_TABLA_COMPRAS);
        db.execSQL(CREAR_TABLA_CONFIG);
        db.execSQL("INSERT INTO " + TABLA_CONFIG + " VALUES (1, 1, 0, 500)");
        //db.execSQL("INSERT INTO \"android_metadata\" VALUES ('en_US')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS android_metadata");
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_RECORDATORIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PRODUCTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_COMPRA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CONFIG);
        onCreate(db);
    }

    public String getPath()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.getPath();
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public Config getConfig(){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLA_CONFIG;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Config td = null;
        if(c != null)
        {
            td = new Config();
            td.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
            td.setLocal((c.getInt(c.getColumnIndex(KEY_LOCAL))));
            td.setPush(c.getInt(c.getColumnIndex(KEY_PUSH)));
            td.setTiendas(c.getInt(c.getColumnIndex(KEY_TIENDAS)));
        }

        return td;
    }
    public int updateConfig(Config g)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCAL, g.getLocal());
        values.put(KEY_PUSH, g.getPush());
        values.put(KEY_TIENDAS, g.getTiendas());

        return db.update(TABLA_CONFIG, values, KEY_ID + " = ?",
                new String[] { String.valueOf(g.get_id()) });
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

    public Recordatorio getRecordatorio(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLA_RECORDATORIO + " WHERE "
                + KEY_ID + " = " + id;

        Log.e("casa_ley", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Recordatorio td = null;
        if(c != null)
        {
            td = new Recordatorio();
            td.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
            td.setTitulo((c.getString(c.getColumnIndex(KEY_TITULO))));
            td.setFecha(c.getLong(c.getColumnIndex(KEY_FECHA)));
        }

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
                td.setFecha(c.getLong(c.getColumnIndex(KEY_FECHA)));
                Log.e("casa_ley", td.get_id() + " " + td.getTitulo());
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

    public void deleteRecordatorio(int tado_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_RECORDATORIO, KEY_ID + " = ?",
                new String[] { String.valueOf(tado_id) });
    }

    public void deleteRecordatorioAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLA_RECORDATORIO);
    }



    public long createCompra(String producto)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, producto);
        long id = db.insert(TABLA_COMPRA, null, values);
        return id;
    }
    public ArrayList<Compra> getAllCompras()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLA_COMPRA;
        Cursor c = db.rawQuery(query, null);
        ArrayList<Compra> lista = new ArrayList<Compra>();
        if(c.moveToFirst())
        {
            do{
                Compra cc = new Compra();
                cc.setNombre(c.getString(c.getColumnIndex(KEY_NOMBRE)));
                cc.set_id( c.getInt(c.getColumnIndex(KEY_ID)));
                lista.add(cc);
            }while(c.moveToNext());
        }
        return lista;
    }
    public void deleteCompra(int tado_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_COMPRA, KEY_ID + " = ?",
                new String[] { String.valueOf(tado_id) });
    }
    public void deleteCompraAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLA_COMPRA);
    }

    public long createProducto(Producto rec) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE, rec.getNombre());
        values.put(KEY_DESC, rec.getDescripcion());

        long id = db.insert(TABLA_PRODUCTO, null, values);

        return id;
    }

    public Producto getProducto(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLA_PRODUCTO + " WHERE "
                + KEY_ID + " = " + id;

        Log.e("casa_ley", selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Producto td = null;
        if(c != null)
        {
            td = new Producto();
            td.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
            td.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));
            td.setDescripcion((c.getString(c.getColumnIndex(KEY_DESC))));
        }

        return td;
    }

    public List<Producto> getAllProductos() {
        List<Producto> todos = new ArrayList<Producto>();
        String selectQuery = "SELECT  * FROM " + TABLA_PRODUCTO;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Producto td = new Producto();
                td.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setNombre((c.getString(c.getColumnIndex(KEY_NOMBRE))));
                td.setDescripcion((c.getString(c.getColumnIndex(KEY_DESC))));

                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

    public void deleteProducto(int tado_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_PRODUCTO, KEY_ID + " = ?",
                new String[] { String.valueOf(tado_id) });
    }

}
