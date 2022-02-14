package com.example.appgestor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appgestor.adapters.ListPuntoVentaAdapter;
import com.example.appgestor.clases.DbBitmapUtility;
import com.example.appgestor.clases.Producto;
import com.example.appgestor.clases.PuntoVenta;
import com.example.appgestor.db.BDHelper;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReportePreciosActivity extends AppCompatActivity {

    TableLayout tblProductos;
    ArrayList<Producto> listProductos, listFilterProductos;
    ImageView btnBack, btnSave;
    TextView txtTitle;
    PuntoVenta pv;
    TableRow filaContenido;
    Boolean cambio = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_precios);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        txtTitle = findViewById(R.id.txtTitle);
        tblProductos = findViewById(R.id.tblProductos);

        if(!getIntent().getStringExtra("puntoVenta").equals("")) {
            Gson gson = new Gson();
            pv = gson.fromJson(getIntent().getStringExtra("puntoVenta"), PuntoVenta.class);
            txtTitle.setText(pv.getNombre());
        }

        consultarProductos();

        crearTabla();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cambio){
                    updateTabla();
                    finish();
                    Intent intent  = new Intent(ReportePreciosActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(ReportePreciosActivity.this, "No hay cambios que guardar", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void consultarProductos(){
        Log.d("Producto Consulta", "Entra consulta");

        BDHelper bdHelper = new BDHelper(ReportePreciosActivity.this, null);
        SQLiteDatabase bd = bdHelper.getReadableDatabase();

        Producto p;

        listProductos = new ArrayList<>();

        Cursor cursor = bd.rawQuery("SELECT * FROM " + BDHelper.getTableProductos(), null);

        while (cursor.moveToNext()){
            p = new Producto(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getFloat(2),
                    cursor.getFloat(3),
                    cursor.getInt(4)
            );
            listProductos.add(p);
        }
    }

    private void crearTabla(){
        String[] cabecera = {"P. Costo", "P. Rvta Mayor", "Stock"};

        TableRow fila = new TableRow(ReportePreciosActivity.this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        fila.setLayoutParams(lp);

        TextView txtProductoCabecera = new TextView(ReportePreciosActivity.this);
        txtProductoCabecera.setText("Productos");
        txtProductoCabecera.setBackgroundResource(R.drawable.edit_text_tabla1);
        txtProductoCabecera.setWidth(300);
        txtProductoCabecera.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        fila.addView(txtProductoCabecera);

        for (int i = 0; i < cabecera.length; i++) {
            TextView txtCabecera = new TextView(ReportePreciosActivity.this);
            txtCabecera.setText(cabecera[i]);
            txtCabecera.setBackgroundResource(R.drawable.edit_text_tabla1);
            txtCabecera.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            fila.addView(txtCabecera);
        }

        tblProductos.addView(fila, 0);

        for (int i = 0; i < listProductos.size(); i++) {
            filaContenido = new TableRow(ReportePreciosActivity.this);
            filaContenido.setLayoutParams(lp);

            tblProductos.addView(addContenido(filaContenido, listProductos.get(i), i));
        }
    }
    private TableRow addContenido(TableRow filaContenido, final Producto p, final int indice){

        final DecimalFormat formateador = new DecimalFormat("######.##");

        final EditText editText = new EditText(ReportePreciosActivity.this);
        editText.setText(p.getNombre());
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setWidth(300);
        editText.setBackgroundResource(R.drawable.edit_text_tabla2);
        editText.setEnabled(false);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        filaContenido.addView(editText);

        final EditText editTextCosto = new EditText(ReportePreciosActivity.this);
        editTextCosto.setText(String.valueOf(p.getpCosto()));
        editTextCosto.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextCosto.setWidth(200);
        editTextCosto.setBackgroundResource(R.drawable.edit_text_tabla2);
        editTextCosto.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        editTextCosto.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        filaContenido.addView(editTextCosto);
        filaContenido.setMinimumWidth(200);

        editTextCosto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cambio = true;
                if (editTextCosto.getText().toString().trim().isEmpty()){
                    listProductos.get(indice).setpCosto(0.00f);
                }else{
                    Float valor = Float.parseFloat(editTextCosto.getText().toString());
                    String valorFormateado = formateador.format(valor);
                    listProductos.get(indice).setpCosto(Float.parseFloat(valorFormateado));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final EditText editTextMayor = new EditText(ReportePreciosActivity.this);
        editTextMayor.setText(String.valueOf(p.getpMayor()));
        editTextMayor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextMayor.setWidth(200);
        editTextMayor.setBackgroundResource(R.drawable.edit_text_tabla2);
        editTextMayor.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        editTextMayor.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        filaContenido.addView(editTextMayor);
        filaContenido.setMinimumWidth(200);

        editTextMayor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cambio = true;
                if (editTextMayor.getText().toString().trim().isEmpty()){
                    listProductos.get(indice).setpMayor(0.00f);
                }else {
                    Float valor = Float.parseFloat(editTextMayor.getText().toString());
                    String valorFormateado = formateador.format(valor);
                    listProductos.get(indice).setpMayor(Float.parseFloat(valorFormateado));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final EditText editTextStock = new EditText(ReportePreciosActivity.this);
        editTextStock.setText(String.valueOf(p.getStock()));
        editTextStock.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextStock.setWidth(200);
        editTextStock.setBackgroundResource(R.drawable.edit_text_tabla2);
        editTextStock.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        editTextStock.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        filaContenido.addView(editTextStock);
        filaContenido.setMinimumWidth(200);

        editTextStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cambio = true;
                if (editTextStock.getText().toString().trim().isEmpty()){
                    listProductos.get(indice).setStock(0);
                }else {
                    Integer valor = Integer.parseInt(editTextStock.getText().toString());
                    listProductos.get(indice).setStock(valor);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return filaContenido;
    }

    private void updateTabla(){
        BDHelper bdHelper = new BDHelper(ReportePreciosActivity.this, null);
        SQLiteDatabase bd = bdHelper.getWritableDatabase();

        for (Producto p: listProductos){
            ContentValues registroProductos = new ContentValues();
            registroProductos.put("nombre", p.getNombre());
            registroProductos.put("p_costo", p.getpCosto());
            registroProductos.put("p_mayor", p.getpMayor());
            registroProductos.put("stock", p.getStock());

            int cantidad = bd.update(bdHelper.getTableProductos(), registroProductos, "id = " + p.getId(), null);

            if(cantidad==1){

            }else{
                Toast.makeText(ReportePreciosActivity.this, "Ocurrio un error al guardar los valores", Toast.LENGTH_SHORT).show();
            }
        }
        bd.close();
    }
}
