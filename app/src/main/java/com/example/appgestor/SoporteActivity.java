package com.example.appgestor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SoporteActivity extends AppCompatActivity {

    String phoneNumber="123";
    private final int TEL_COD = 100;
    TextView txtNumero;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soporte);

        txtNumero = findViewById(R.id.txtNumeroSoporte);


        txtNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, TEL_COD);

                } else{
                    OlderVersions(phoneNumber);

                }
            }
            private void OlderVersions(String phoneNumber){
                Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));

                int result = checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE);
                if ( result == PackageManager.PERMISSION_GRANTED){

                    startActivity(intentCall);}
                else{
                    Toast.makeText(SoporteActivity.this, "Acceso no autorizado", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case TEL_COD:
                String permisos = permissions[0];
                int result = grantResults[0];
                if (permisos.equals(Manifest.permission.CALL_PHONE)){
                    if (result == PackageManager.PERMISSION_GRANTED){

                        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phoneNumber));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) return;
                        startActivity(intentCall);

                    }
                    else{
                        Toast.makeText(SoporteActivity.this, "Acceso no autorizado", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }

    }
}
