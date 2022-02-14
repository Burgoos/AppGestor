package com.example.appgestor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgestor.clases.DbBitmapUtility;
import com.example.appgestor.clases.PuntoVenta;
import com.example.appgestor.db.BDHelper;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PuntoVentaDetalleActivity extends AppCompatActivity {
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;

    ImageView imgCamara;
    TextView txtNombre;
    TextView txtDireccion;
    PuntoVenta pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punto_venta_detalle);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        imgCamara = findViewById(R.id.imgCamara);
        txtNombre = findViewById(R.id.txtNombrePV);
        txtDireccion = findViewById(R.id.txtDireccionPV);

        if(!getIntent().getStringExtra("puntoVenta").equals("")) {
            Gson gson = new Gson();
            pv = gson.fromJson(getIntent().getStringExtra("puntoVenta"),PuntoVenta.class);
            txtNombre.setText(pv.getNombre());
            txtDireccion.setText(pv.getDirección());
            Toast.makeText(PuntoVentaDetalleActivity.this, pv.getCodigo(), Toast.LENGTH_SHORT).show();
            if(pv.getFoto()!=null){
                loadImageFromBD();
            }
        }

        imgCamara.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                }
            }
        });
    }

    private void loadImageFromBD(){
        imgCamara.setImageBitmap(pv.getFoto());
    }

    public void updateFoto(){
        Toast.makeText(PuntoVentaDetalleActivity.this,"Entra Update", Toast.LENGTH_SHORT).show();
        BDHelper bdHelper = new BDHelper(PuntoVentaDetalleActivity.this, null);
        SQLiteDatabase bd = bdHelper.getWritableDatabase();

        ContentValues registroPuntoVenta = new ContentValues();
        registroPuntoVenta.put("codigo", pv.getCodigo());
        registroPuntoVenta.put("nombre", pv.getNombre());
        registroPuntoVenta.put("direccion", pv.getDirección());
        registroPuntoVenta.put("latitud", pv.getLatitud());
        registroPuntoVenta.put("longitud", pv.getLongitud());
        registroPuntoVenta.put("foto", DbBitmapUtility.getBytes(pv.getFoto()));

        int cantidad = bd.update(bdHelper.getTablePuntoVenta(), registroPuntoVenta, "codigo = " + pv.getCodigo(), null);
        bd.close();

        if(cantidad==1){

        }else{
            Toast.makeText(PuntoVentaDetalleActivity.this, "Ocurrio un error al guardar la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }else{
                //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            pv.setFoto(photo);
            updateFoto();
            imgCamara.setImageBitmap(photo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
