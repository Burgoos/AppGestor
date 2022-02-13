package com.example.appgestor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgestor.db.BDHelper;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences myPreferences;
    SharedPreferences.Editor myEditor;

    Button btnLogin;
    EditText txtCorreo, txtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myPreferences =  PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        myEditor = myPreferences.edit();

        txtCorreo = findViewById(R.id.txtUsuario_Login);
        txtPassword = findViewById(R.id.txtPassword_Login);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Consultar();
            }
        });
    }

    public void Consultar(){
        BDHelper bdHelper = new BDHelper(LoginActivity.this, null);
        SQLiteDatabase bd = bdHelper.getWritableDatabase();

        if(txtCorreo.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "Debe llenar los campos", Toast.LENGTH_SHORT).show();
        }else{
            Cursor fila = bd.rawQuery("SELECT * FROM " + BDHelper.getTableUsuario() +
                    " WHERE correo = '" + txtCorreo.getText().toString().trim() + "'" +
                    " AND password = '" + txtPassword.getText().toString().trim() + "'", null);

            if(fila.moveToFirst()){
                myEditor.putString("idUsuario", fila.getString(0));
                myEditor.putString("nombreUsuario", fila.getString(1));
                myEditor.putString("correoUsuario", fila.getString(2));
                myEditor.putString("fotoUsuario", fila.getString(4));
                myEditor.commit();
                finish();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(LoginActivity.this, "Usuario y/o contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }
        }
        bd.close();
    }
}
