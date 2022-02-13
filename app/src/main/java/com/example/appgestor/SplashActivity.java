package com.example.appgestor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.example.appgestor.db.BDHelper;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;
    String correo;
    private final int DURACION_SPLASH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        myPreferences =  PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        myEditor = myPreferences.edit();

        correo = myPreferences.getString("correoUsuario", "");

        //this.deleteDatabase("gestor.db");
        //insertInicial();
        cargarSplash();
    }


    void cargarSplash(){
        new Handler().postDelayed(new Runnable(){
            public void run(){
                if(correo.equals("")){
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                finish();
            };
        }, DURACION_SPLASH);
    }

    void insertInicial(){
        BDHelper bdHelper = new BDHelper(SplashActivity.this, null);
        SQLiteDatabase bd = bdHelper.getWritableDatabase();

        ContentValues registroUsuario = new ContentValues();
        registroUsuario.put("nombre", "Alexander Burgos Saba");
        registroUsuario.put("correo", "burgos@gmail.com");
        registroUsuario.put("password", "123456");
        registroUsuario.put("foto", "");
        bd.insert(bdHelper.getTableUsuario(),null, registroUsuario);

        ContentValues registroPuntoVenta = new ContentValues();
        registroPuntoVenta.put("codigo", "409183");
        registroPuntoVenta.put("nombre", "YOSLY AMALI SEGUILAR");
        registroPuntoVenta.put("direccion", "REAL S/N Urb: ESQ. 10 NOVIEMBRE");
        registroPuntoVenta.put("latitud", "-12.041749");
        registroPuntoVenta.put("longitud", "-77.075564");
        registroPuntoVenta.put("foto", "");
        bd.insert(bdHelper.getTablePuntoVenta(),null, registroPuntoVenta);

        ContentValues registroPuntoVenta2 = new ContentValues();
        registroPuntoVenta2.put("codigo", "409184");
        registroPuntoVenta2.put("nombre", "METRO ALFONSO UGARTE");
        registroPuntoVenta2.put("direccion", "AV. ALFONSO UGARTE 740");
        registroPuntoVenta2.put("latitud", "-12.106730");
        registroPuntoVenta2.put("longitud", "-76.996891");
        bd.insert(bdHelper.getTablePuntoVenta(),null, registroPuntoVenta2);

        ContentValues registroPuntoVenta3 = new ContentValues();
        registroPuntoVenta3.put("codigo", "409185");
        registroPuntoVenta3.put("nombre", "TOTTUS ZORRITOS");
        registroPuntoVenta3.put("direccion", "AV. COLONIAL 1520");
        registroPuntoVenta3.put("latitud", "-12.069397");
        registroPuntoVenta3.put("longitud", "-77.162514");
        bd.insert(bdHelper.getTablePuntoVenta(),null, registroPuntoVenta3);

        bd.close();
    }
}
