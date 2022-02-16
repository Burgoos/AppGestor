package com.example.appgestor;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgestor.clases.DbBitmapUtility;
import com.example.appgestor.clases.PuntoVenta;
import com.example.appgestor.db.BDHelper;
import com.google.gson.Gson;

public class PuntoVentaDetalleActivity extends AppCompatActivity {
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;

    ImageView imgCamara;
    TextView txtNombre;
    TextView txtDireccion;
    PuntoVenta pv;
    Button btnVisitar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_punto_venta_detalle);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        imgCamara = findViewById(R.id.imgCamara);
        txtNombre = findViewById(R.id.txtNombrePV);
        txtDireccion = findViewById(R.id.txtDireccionPV);
        btnVisitar = findViewById(R.id.btnVisitar);

        if(!getIntent().getStringExtra("puntoVenta").equals("")) {
            Gson gson = new Gson();
            pv = gson.fromJson(getIntent().getStringExtra("puntoVenta"),PuntoVenta.class);
            txtNombre.setText(pv.getNombre());
            txtDireccion.setText(pv.getDirección());
            if(pv.getFoto()!=null){
                loadImageFromBD();
            }
        }

        btnVisitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Intent intent = new Intent(PuntoVentaDetalleActivity.this, ReportePreciosActivity.class);
                intent.putExtra("puntoVenta", gson.toJson(pv));
                startActivity(intent);
            }
        });
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
        BDHelper bdHelper = new BDHelper(PuntoVentaDetalleActivity.this, null);
        SQLiteDatabase bd = bdHelper.getWritableDatabase();

        ContentValues registroPuntoVenta = new ContentValues();

        registroPuntoVenta.put("foto", DbBitmapUtility.getBytes(pv.getFoto()));

        int cantidad = bd.update(bdHelper.getTablePuntoVenta(), registroPuntoVenta, "codigo = '" + pv.getCodigo() + "'", null);
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
