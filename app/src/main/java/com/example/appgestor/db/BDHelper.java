package com.example.appgestor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "gestor.db";
    private static final String TABLE_PUNTO_VENTA = "t_punto_venta";
    private static final String TABLE_USUARIO = "t_usuario";
    private static final String TABLE_PRODUCTOS = "t_productos";

    public BDHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NOMBRE, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_USUARIO + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "correo TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "foto BLOB)");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PUNTO_VENTA + "(" +
                "codigo TEXT PRIMARY KEY," +
                "nombre TEXT NOT NULL," +
                "direccion TEXT NOT NULL," +
                "latitud DECIMAL(10,8) NOT NULL," +
                "longitud DECIMAL(11,8) NOT NULL," +
                "foto BLOB)");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_PRODUCTOS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "p_costo REAL NOT NULL," +
                "p_mayor REAL NOT NULL," +
                "stock INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static String getTablePuntoVenta() {
        return TABLE_PUNTO_VENTA;
    }

    public static String getTableUsuario() {
        return TABLE_USUARIO;
    }

    public static String getTableProductos() {
        return TABLE_PRODUCTOS;
    }
}
